package com.chenlm.cloud.zuul;

//import io.jmnarloch.spring.cloud.ribbon.predicate.MetadataAwarePredicate;
//import io.jmnarloch.spring.cloud.ribbon.support.RibbonDiscoveryRuleAutoConfiguration;

//@Configuration
public class TestConfiguration {
//    @Bean
    public ServerSelector serverSelector1() {
        return new SimpleHeaderServerSelector("x-hello");
    }
//    @Bean
    public ServerSelector serverSelector2() {
        return new SimpleHeaderServerSelector("x-world");
    }
}