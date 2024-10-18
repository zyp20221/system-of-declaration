const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  configureWebpack: {
    devServer: {
      client: { overlay: false }
    }
  },
  devServer: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080', // 后端API服务器地址
        changeOrigin: true,
        pathRewrite: {
          '^/api': ''
        }
      }
    },
    port:7000
  }

})
