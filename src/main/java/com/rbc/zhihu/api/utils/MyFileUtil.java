package com.rbc.zhihu.api.utils;

import org.springframework.web.multipart.MultipartFile;
import java.io.*;

import java.nio.file.Files;

/**
 * @author DingYihang
 */
public class MyFileUtil {

    /**
     * 将 File 转换为 MultipartFile。
     *
     * @param file      要转换的文件
     * @param fieldName 字段名，通常用于表单中的文件字段名
     * @return 转换后的 MultipartFile
     * @throws IOException 如果发生I/O错误
     */
    public static MultipartFile fileToMultipartFile(File file, String fieldName) throws IOException {
        try {
            if (file == null || !file.exists()) {
                throw new FileNotFoundException("文件未找到：" + file);
            }
            byte[] content = Files.readAllBytes(file.toPath());
            return new ByteArrayMultipartFile(content, file.getName(), fieldName, Files.probeContentType(file.toPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            // 删除临时文件
            file.delete();
        }
    }

    /**
     * 将 MultipartFile 转换为 File。
     *
     * @param multipartFile 要转换的 MultipartFile
     * @return 转换后的 File
     * @throws IOException 如果发生I/O错误
     */
    public static File multipartFileToFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            throw new IOException("传入的MultipartFile为空");
        }
        String originalFilename = multipartFile.getOriginalFilename();
        String tempFileSuffix = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf('.')) : ".tmp";
        File tempFile = File.createTempFile("temp", tempFileSuffix);
        try (InputStream ins = multipartFile.getInputStream();
             OutputStream os = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = ins.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        }
        return tempFile;
    }

    /**
     * 内置一个简单的 MultipartFile 实现类，用于File转换
     */
    private static class ByteArrayMultipartFile implements MultipartFile {
        private final byte[] content;
        private final String name;
        private final String originalFilename;
        private final String contentType;

        /**
         * 构造函数
         *
         * @param content         文件内容
         * @param originalFilename 文件原始名字
         * @param name            字段名
         * @param contentType     文件类型
         */
        public ByteArrayMultipartFile(byte[] content, String originalFilename, String name, String contentType) {
            this.content = content;
            this.originalFilename = originalFilename;
            this.name = name;
            this.contentType = contentType;
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public String getOriginalFilename() {
            return this.originalFilename;
        }

        @Override
        public String getContentType() {
            return this.contentType;
        }

        @Override
        public boolean isEmpty() {
            return (this.content == null || this.content.length == 0);
        }

        @Override
        public long getSize() {
            return this.content.length;
        }

        @Override
        public byte[] getBytes() {
            return this.content;
        }

        @Override
        public InputStream getInputStream() {
            return new ByteArrayInputStream(this.content);
        }

        @Override
        public void transferTo(File dest) throws IOException, IllegalStateException {
            try (OutputStream os = new FileOutputStream(dest)) {
                os.write(this.content);
            }
        }
    }

}