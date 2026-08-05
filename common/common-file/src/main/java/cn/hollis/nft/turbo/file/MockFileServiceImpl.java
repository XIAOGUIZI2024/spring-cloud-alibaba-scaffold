package cn.hollis.nft.turbo.file;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;

/**
 * Mock 文件服务（dev/test 环境使用，直接返回成功）
 *
 * @author hollis
 */
@Slf4j
@Setter
public class MockFileServiceImpl implements FileService {

    @Override
    public boolean upload(String path, InputStream fileStream) {
        log.info("MockFileServiceImpl upload, path={}", path);
        return true;
    }

}
