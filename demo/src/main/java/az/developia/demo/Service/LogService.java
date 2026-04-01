package az.developia.demo.Service;

import az.developia.demo.Entity.LogEntity;
import az.developia.demo.Mapper.LogMapper;
import az.developia.demo.Repo.LogRepo;
import az.developia.demo.Request.LogRequest;
import az.developia.demo.Response.LogResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LogService {
    private final LogRepo logRepo;

    public void add(String message,String logType){
        LogEntity logEntity = new LogEntity();
        logEntity.setMessage(message);
        logEntity.setLogType(logType);

        logRepo.save(logEntity);
    }

    public List<LogResponse> Logs(){
        return LogMapper.toDTOList(logRepo.findAll());
    }
}