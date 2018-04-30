package com.livedrof.activiti;

import org.activiti.engine.*;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Activiti七大Service服务接口
 * 1、RepositoryService:流程仓库Service
 * 2、IdentifyService:身份Service
 * 3、RuntimeService:运行时Service
 * 4、TaskService:任务Service
 * 5、FormService: 表单Service
 * 6、HistoryService:历史Service
 * 7、ManagementService:  引擎管理Service
 */
public class Sayhelloleave2 {
    @Test
    public void testStartProcess() {
        // 创建工作流引擎，使用内存数据库
        ProcessEngine processEngine = ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration().buildProcessEngine();
        // 布署流程定义文件
        RepositoryService repositoryService = processEngine.getRepositoryService();
        repositoryService.createDeployment().addClasspathResource("sayhelloleave2.bpmn").deploy();
        //取得流程定义
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().singleResult();
        assertEquals("leavesayhello", processDefinition.getKey());
        // 启动流程并返回实例
        RuntimeService runtimeService = processEngine.getRuntimeService();

        Map<String, Object> vars = new HashMap<String, Object>();
        vars.put("applyUser", "employee1");
        vars.put("days", 3);

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("leavesayhello",vars);
        assertNotNull(processInstance);

        System.out.println("pid=" + processInstance.getId() + ", pdid=" + processInstance.getProcessDefinitionId());

        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery().taskCandidateGroup("deptLeader").singleResult();
        // 签收此任务归用户leaderUser所有
        taskService.claim(task.getId(), "leaderUser");

        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("approved", true);

        taskService.complete(task.getId(), variables);

        HistoryService historyService = processEngine.getHistoryService();
        HistoricProcessInstanceQuery historicProcessInstanceQuery = historyService.createHistoricProcessInstanceQuery().finished();
        System.out.println(historicProcessInstanceQuery.count());
    }
}
