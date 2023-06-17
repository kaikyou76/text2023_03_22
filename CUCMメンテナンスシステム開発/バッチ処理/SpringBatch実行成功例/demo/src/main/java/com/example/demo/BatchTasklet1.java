package com.example.demo;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * @Component: Bean として登録するため
 * @StepScope: Stepごとにスコープを定義。
 *      StepScope インタフェースは次のアノテーションを持ちます。
 *      @Scope(value = "step", proxyMode = ScopedProxyMode.TARGET_CLASS)
 */
@Component
@StepScope
public class BatchTasklet1 implements Tasklet {

    /**
     * job の実行時に指定されたパラメータがセットされます。
     * param1=テスト1 param2=202 param3=テスト3 の場合、「テスト1」です。
     */
    @Value("#{jobParameters[param1]}")
    private String param1;


    /**
     * job の実行時に指定されたパラメータがセットされます。
     * param1=テスト1 param2=202 param3=テスト3 の場合、「202」です。
     */
    @Value("#{jobParameters[param2]}")
    private Integer param2;

    @Value("#{jobParameters[param3]}")
    private String param3;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext context) {
        System.out.println("Batch Task1 is called !");
        System.out.println("param1: " + param1);
        System.out.println("param2: " + param2);
        System.out.println("param3 " + param3);

        contribution.setExitStatus(new ExitStatus("success"));

        return RepeatStatus.FINISHED;
    }
}

