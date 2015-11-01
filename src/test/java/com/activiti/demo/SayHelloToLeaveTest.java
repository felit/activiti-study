package com.activiti.demo;

import org.activiti.engine.*;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

/**
 */
public class SayHelloToLeaveTest {
    private ProcessEngine processEngine;
    private RepositoryService repositoryService;

    @BeforeTest
    public void setUp() {
        this.processEngine = ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration().buildProcessEngine();
        this.repositoryService = processEngine.getRepositoryService();

    }

    @Test
    public void testStartProcess() {
        String bpmnFileName = "leave2.bpmn";
        repositoryService.createDeployment().addClasspathResource(bpmnFileName).deploy();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().singleResult();

        RuntimeService runtimeService = processEngine.getRuntimeService();
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("applyUser", "employee1");
    }
    @Test
    public void testProcess() {
        String bpmnFile = "leave2.bpmn";
        this.repositoryService.createDeployment().addInputStream("leave.bpmn", this.getClass().getClassLoader().getResourceAsStream(bpmnFile)).deploy();

        ProcessDefinition processDefinition = this.repositoryService.createProcessDefinitionQuery().singleResult();
        System.out.println(processDefinition.getKey());
        System.out.println(processDefinition.getId());

        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("applyUser", "employee1");
        variables.put("days", 3);
        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("leave", variables);

        TaskService taskService = this.processEngine.getTaskService();
        Task task = taskService.createTaskQuery().taskCandidateGroup("deptLeader").singleResult();
        taskService.claim(task.getId(), "leaderUser");

        variables = new HashMap<String, Object>();
        variables.put("approved", true);
        taskService.complete(task.getId(),variables);

        HistoryService historyService = processEngine.getHistoryService();
        long count = historyService.createHistoricProcessInstanceQuery().finished().count();
        System.out.println(count);


    }
}
