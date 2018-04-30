package com.livedrof.activiti;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

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
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("leavesayhello");
        assertNotNull(processInstance);

        System.out.println("pid=" + processInstance.getId() + ", pdid=" + processInstance.getProcessDefinitionId());

    }
}
