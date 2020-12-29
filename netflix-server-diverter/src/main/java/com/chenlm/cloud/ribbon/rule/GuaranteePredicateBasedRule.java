package com.chenlm.cloud.ribbon.rule;

import com.google.common.base.Optional;
import com.netflix.loadbalancer.AbstractServerPredicate;
import com.netflix.loadbalancer.ClientConfigEnabledRoundRobinRule;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import com.chenlm.cloud.ribbon.support.RibbonFilterContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Chenlm
 */
public abstract class GuaranteePredicateBasedRule extends ClientConfigEnabledRoundRobinRule {
    private static final Logger logger = LoggerFactory.getLogger(GuaranteePredicateBasedRule.class);


    /**
     * Method that provides an instance of {@link AbstractServerPredicate} to be used by this class.
     */
    public abstract AbstractServerPredicate getPredicate();

    /**
     * Get a server by calling {@link AbstractServerPredicate#chooseRandomlyAfterFiltering(java.util.List, Object)}.
     * The performance for this method is O(n) where n is number of servers to be filtered.
     */
    @Override
    public Server choose(Object key) {
        ILoadBalancer lb = getLoadBalancer();
        Optional<Server> server = getPredicate().chooseRoundRobinAfterFiltering(lb.getAllServers(), key);

        if (server.isPresent()) {
            logger.debug("根据 {} 策略选择到服务 {}", RibbonFilterContextHolder.getCurrentContext().getAttributes(), server.get());
            return server.get();
        } else {
            Server result = super.choose(key);
            logger.info("根据 {} 策略未选择到服务，使用保底策略选择到: {}", RibbonFilterContextHolder.getCurrentContext().getAttributes(), result);
            return result;
        }
    }
}