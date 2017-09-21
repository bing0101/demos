package com.bing.shardingjdbc.spring.aspect;

import com.dangdang.ddframe.rdb.transaction.soft.bed.BEDSoftTransaction;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.SQLException;

/**
 * Created by Administrator on 2017/9/21.
 */
@Aspect
@Component
public class ShardingJdbcBEDSoftTransactionAspect {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private BEDSoftTransaction bedSoftTransaction;

    @Pointcut("@annotation(BEDSoftTransactional)")
    public void bedSoftTxPointcut() {}

    @Around("bedSoftTxPointcut()")
    public void around(ProceedingJoinPoint pjp) {
        bedSoftTransaction.begin();//todo 开启事务需要传递connection
        try {
            pjp.proceed();
        } catch (Throwable throwable) {
            logger.error("执行业务代码异常！", throwable);
        } finally {
            try {
                bedSoftTransaction.end();
            } catch (SQLException e) {
                logger.error("bedSoftTransaction end fail!", e);
            }
        }
    }
}
