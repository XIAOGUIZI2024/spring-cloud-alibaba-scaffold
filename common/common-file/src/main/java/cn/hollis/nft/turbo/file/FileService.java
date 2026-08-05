package cn.hollis.nft.turbo.file;

import java.io.InputStream;

/**
 * 文件服务
 *
 * @author hollis
 */
public interface FileService {

    /**
     * 文件上传
     *
     * @param path       对象完整路径（不含 Bucket 名称），如 profile/1/avatar.png
     * @param fileStream 文件流
     * @return 上传是否成功
     */
    boolean upload(String path, InputStream fileStream);

}
