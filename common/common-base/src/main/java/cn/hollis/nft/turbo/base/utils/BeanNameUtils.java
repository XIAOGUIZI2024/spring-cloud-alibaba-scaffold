package cn.hollis.nft.turbo.base.utils;

import com.google.common.base.CaseFormat;

/**
 * Bean 名称工具
 *
 * @author hollis
 */
public class BeanNameUtils {

    /**
     * 把一个策略名称转换成 beanName
     * <pre>
     *     如 WEN_CHANG ，ChainService -> wenChangChainService
     * </pre>
     *
     * @param strategyName 策略名称
     * @param serviceName 服务名
     * @return beanName
     */
    public static String getBeanName(String strategyName, String serviceName) {
        //将服务转换成小写字母开头的驼峰形式，如 A_BCD 转成 aBcd
        return CaseFormat.UPPER_UNDERSCORE.converterTo(CaseFormat.LOWER_CAMEL).convert(strategyName) + serviceName;
    }
}
