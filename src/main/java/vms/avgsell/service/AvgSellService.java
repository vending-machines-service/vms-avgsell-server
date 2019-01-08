package vms.avgsell.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import vms.avgsell.dto.MachineDTO;
import vms.avgsell.dto.SensorData;
import vms.avgsell.entity.MachineJPA;
import vms.avgsell.entity.SensorProductJpa;
import vms.avgsell.repo.AvgSellRepository;
import vms.avgsell.repo.MachinesSqlRepository;

@Service
@Slf4j
@EnableBinding(Sink.class)
public class AvgSellService {

	@Autowired
	AvgSellRepository repo;

	@Autowired
	MachinesSqlRepository machineRepo;

	ObjectMapper mapper = new ObjectMapper();
	long timestameAvgPeriod = System.currentTimeMillis();
	long timestameUpdatePeriod = System.currentTimeMillis();
	@Value("${avg_period_avgsell_service:5000}")
	long avgPeriod;
	@Value("${update_period_avgsell_service:5000}")
	long updatePeriod;

	Map<Integer, Map<Integer, Integer>> machinesSensorsQuantity = new HashMap<>();
	Map<Integer, Map<Integer, Integer>> machinesStateLast = new HashMap<>();
	Map<Integer, Map<Integer, Integer>> machinesSensorProduct = new HashMap<>();

	@StreamListener(Sink.INPUT)
	public void getStaticInfoBySensor(String jsonSensor) throws JsonParseException, JsonMappingException, IOException {
		SensorData sensorProd = mapper.readValue(jsonSensor, SensorData.class);
		addToMapSensors(sensorProd);
		addMapSensorProduct(sensorProd);
		if (System.currentTimeMillis() - timestameAvgPeriod > avgPeriod) {
			writeRecordsInBD(machinesSensorsQuantity);
			timestameAvgPeriod = System.currentTimeMillis();
		}

		if (System.currentTimeMillis() - timestameUpdatePeriod > updatePeriod) {
			machinesSensorProduct.clear();
			timestameUpdatePeriod = System.currentTimeMillis();
		}

	}

	private void addMapSensorProduct(SensorData sensorProd) {
		if (machinesSensorProduct.get(sensorProd.machineId) == null) {
			MachineJPA jpa = machineRepo.findById(sensorProd.machineId).orElse(null);
			if (jpa != null) {
				MachineDTO dto = jpa.convertJPAtoDTO();
				machinesSensorProduct.put(dto.machineId, dto.sensorProduct);
			}
		}
	}
	
	private void addToMapSensors(SensorData sensorProd) {
		machinesSensorsQuantity.putIfAbsent(sensorProd.machineId, new HashMap<>());
		Map<Integer, Integer> sensorsQuantity = machinesSensorsQuantity.get(sensorProd.machineId);
		sensorsQuantity.putIfAbsent(sensorProd.sensorId, 0);
		
		machinesStateLast.putIfAbsent(sensorProd.machineId, new HashMap<>());
		Map<Integer, Integer> sensorsStateLast = machinesStateLast.get(sensorProd.machineId);
		sensorsStateLast.putIfAbsent(sensorProd.sensorId, sensorProd.value);
		int lastQuantity = sensorsStateLast.get(sensorProd.sensorId);
		
		if (lastQuantity > sensorProd.value) {
			sensorsQuantity.merge(sensorProd.sensorId, lastQuantity - sensorProd.value, (v1, v2) -> v1 + v2);
			sensorsStateLast.put(sensorProd.sensorId, sensorProd.value);
		}
		
	}
	
	public void writeRecordsInBD(Map<Integer, Map<Integer, Integer>> machinesSensorsQuantity) {
		log.warn("machinesSensorsQuantity: {}", machinesSensorsQuantity);
		int machineId = -1;
		for (Map.Entry<Integer, Map<Integer, Integer>> map : machinesSensorsQuantity.entrySet()) {
			machineId = map.getKey();
			for (Map.Entry<Integer, Integer> mapp : map.getValue().entrySet()) {
				log.warn("SENSOR ID TO SAVE: {}");
				Map<Integer, Integer> sensProd = machinesSensorProduct.get(machineId);
				log.warn("NEXT STEP SENSOR ID TO SAVE: {}", sensProd.get(mapp.getKey()));
				if (sensProd != null && sensProd.get(mapp.getKey()) != null) {
					writeRecord(machineId, sensProd.get(mapp.getKey()), mapp.getValue());
				}
			}
		}
	}

	@Transactional
	private void writeRecord(int machineId, int productId, int count) {
		SensorProductJpa record = new SensorProductJpa(LocalDate.now(), machineId, productId, count);
		repo.save(record);
	}
}
