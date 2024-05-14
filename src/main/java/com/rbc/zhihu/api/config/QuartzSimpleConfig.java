package com.rbc.zhihu.api.config;


import com.rbc.zhihu.api.task.QuartzSimpleTask;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置简单的Quartz任务
 * @author DingYihang
 */
@Configuration
public class QuartzSimpleConfig {

    /**
     * 指定具体的定时任务类
     *
     * @return 上传任务的详细信息
     */
    @Bean
    public JobDetail uploadTaskDetail() {
        return JobBuilder.newJob(QuartzSimpleTask.class)
                .withIdentity("QuartzSimpleTask")
                .storeDurably().build();
    }

    /**
     * 创建任务触发器
     * @return 任务触发器
     */
    @Bean
    public Trigger uploadTaskTrigger() {
        // 设置触发执行的方式
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0 9 23 6 5 ?");
        // 返回任务触发器
        return TriggerBuilder.newTrigger().forJob(uploadTaskDetail())
                .withIdentity("QuartzSimpleTask")
                .withSchedule(scheduleBuilder)
                .build();
    }
}
