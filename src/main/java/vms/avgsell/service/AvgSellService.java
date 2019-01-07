package vms.avgsell.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import vms.avgsell.dto.SensorData;
import vms.avgsell.entity.SensorProductJpa;
import vms.avgsell.repo.AvgSellRepository;

@Service
@Slf4j
@EnableBinding(Sink.class)
public class AvgSellService {

	@Autowired
	AvgSellRepository repo;

	ObjectMapper mapper = new ObjectMapper();
	LocalDate dateRecord = LocalDate.now();

	Map<Integer, Map<Integer, Integer>> machinesSensorsQuantity = new HashMap<>();
	Map<Integer, Map<Integer, Integer>> machinesStateLast = new HashMap<>();

	@StreamListener(Sink.INPUT)
	public void getStaticInfoBySensor(String jsonSensor) throws JsonParseException, JsonMappingException, IOException {
		SensorData sensorProd = mapper.readValue(jsonSensor, SensorData.class);
		log.info("MACHINE: {}; SENSOR:{}; VALUE: {}] ", sensorProd.getMachineId(),
          sensorProd.getSensorId(), sensorProd.getValue());
		addToMapSensors(sensorProd);
		LocalDate currentDate = LocalDate.now();
		if (currentDate.isAfter(dateRecord)) {
			log.info("DATA SAVED IN DB");
			writeRecordsInBD(machinesSensorsQuantity);
			machinesSensorsQuantity.clear();
			dateRecord = currentDate;
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
		int machineId = -1;
		for (Map.Entry<Integer, Map<Integer, Integer>> map : machinesSensorsQuantity.entrySet()) {
			machineId = map.getKey();
			for (Map.Entry<Integer, Integer> mapp : map.getValue().entrySet()) {
				SensorData sens = new SensorData(machineId, mapp.getKey(), -1);
				writeRecord(sens, mapp.getValue());
			}
		}
	}

	@Transactional
	private void writeRecord(SensorData sensorProd, int count) {
		SensorProductJpa record = new SensorProductJpa(dateRecord, sensorProd.machineId, sensorProd.sensorId, count, 0);
		repo.save(record);

	}

	// public List<SensorProductJpa> getAllRecords() {
	// return repo.findAll();
	// }
	//
	// public Map<Integer, Map<Integer, Integer>> getMachinesSensorsQuantity() {
	// return machinesSensorsQuantity;
	// }

}
