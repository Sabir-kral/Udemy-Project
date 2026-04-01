package az.developia.demo.Mapper;


import az.developia.demo.Entity.LogEntity;
import az.developia.demo.Response.LogResponse;

import java.util.List;
import java.util.stream.Collectors;

public class LogMapper {
    public static LogResponse toDTO(LogEntity logEntity){
        LogResponse logResponse = new LogResponse();
        logResponse.setId(logEntity.getId());
        logResponse.setMessage(logEntity.getMessage());
        logResponse.setLogType(logEntity.getLogType());

        return logResponse;
    }
    public static List<LogResponse> toDTOList(List<LogEntity> entities){
        return entities.stream().map(LogMapper::toDTO).collect(Collectors.toList());
    }
}