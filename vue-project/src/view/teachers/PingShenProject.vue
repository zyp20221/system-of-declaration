<template>
    <div id="app">
      <el-container>
        <el-header
        style="
            background-color: #4caf50;
            color: white;
            display: flex;
            justify-content: space-between;
            align-items: center;
            height: 60px;
            padding: 0 20px;
          "
        >
          <span style="font-size: 24px">申报与评审系统</span>
          <div style="display: flex; align-items: center;">
            <el-button
              icon="el-icon-bell"
              @click="showNotifications"
              style="background-color: transparent; color: white; border: none; margin-right: 20px; font-size: 20px; position: relative;"
            >
              <span v-if="showNotificationDot" class="notification-dot"></span>
            </el-button>
            <router-link to="/login">
              <el-avatar
                size="40"
                src="https://via.placeholder.com/100"
                alt="个人图像"
              />
            </router-link>
          </div>
        </el-header>
        <el-container>
          <el-aside width="200px" style="background-color: #f5f5f5">
          <el-menu :default-active="activeMenu" class="el-menu-vertical-demo">
            <el-menu-item index="1">
              <router-link
                to="/teachers"
                style="font-size: large; font-weight: bold"
                >主页</router-link
              >
            </el-menu-item>
            <el-submenu index="2">
              <template #title>申报管理</template>
              <el-menu-item index="2-1">
                <router-link to="/teacherdeclare">申报项目</router-link>
              </el-menu-item>
            </el-submenu>
            <el-menu-item index="3">
              <router-link to="/reviewList">指导项目</router-link>
            </el-menu-item>
            <el-menu-item index="4">
              <router-link to="/pingshenlist">评审项目</router-link>
            </el-menu-item>
            <el-menu-item index="5">
              <router-link to="/teacherspersonal">个人中心</router-link>
            </el-menu-item>
          </el-menu>
        </el-aside>
          <el-main>
            <div class="form-container">
              <el-form :model="form" label-width="100px">
                <el-form-item label="项目名称:">
                  <el-input
                    v-model="form.projectName"
                    :disabled="form.projectName !== ''"
                    placeholder="项目名称"
                  ></el-input>
                </el-form-item>
                <el-form-item label="申报书名称:">
                  <el-input
                    v-model="form.declarationName"
                    placeholder="申报书名称"
                  ></el-input>
                </el-form-item>
                <el-form-item label="项目简介(500字以内):">
                  <el-input
                    type="textarea"
                    v-model="form.projectBrief"
                    :rows="4"
                    placeholder="请输入项目简介"
                  ></el-input>
                </el-form-item>
                <el-form-item label="申报内容">
                  <el-input
                    type="textarea"
                    v-model="form.declarationContent"
                    :rows="4"
                  ></el-input>
                </el-form-item>
                <el-form-item label="评审意见">
                  <el-input
                    type="textarea"
                    v-model="form2.pingshenAdvice"
                    :rows="4"
                  ></el-input>
                </el-form-item>
                <el-form-item>
                <el-button type="primary" @click="downloadAttachment"
                  >下载附件</el-button
                >
              </el-form-item>
                <el-form-item>
                  <div style="display: flex; justify-content: center; gap: 20px;">
                    <el-button type="primary" @click="submitForm">提交评审</el-button>
                    <el-button type="danger" @click="rejectForm">返回修改</el-button>
                  </div>
                </el-form-item>
              </el-form>
            </div>
          </el-main>
        </el-container>
      </el-container>
      <div
        style="
          background-color: #4caf50;
          color: white;
          text-align: center;
          padding: 10px;
        "
      >
        用户单位：zyp 版权所有：zyp 访问量：0000
      </div>
    </div>
  </template>
  
  <script>
  import axios from 'axios';
  
  export default {
    data() {
      return {
        form: {
          projectName: "",
          projectBrief: "",
          declarationContent: "",
          declarationName: "",
        },
        form2: {
          pingshenAdvice: "",
        },
        forms3:{
          studentsJindu: 1,
          teacherJindu: 1,
          zhuanjiaJindu: 0,
          schoolJindu: 0 ,
          collegeJindu: 0,
          fabuJindu: 0
        },
        forms4:{
          studentsJindu: 0,
          teacherJindu: 0,
          zhuanjiaJindu: 0,
          schoolJindu: 0 ,
          collegeJindu: 0,
          fabuJindu: 0
        },
        bid:this.$route.query.bid,
        notifications: [],
        form10:{
          lastNotificationCount: 0,
        },
      };
    },
    computed: {
      showNotificationDot() {
        // 只有当通知数量大于上次通知数量时才显示红点
        return this.notifications.length > this.form10.lastNotificationCount; // 只有当数量增加时显示红点
      },
    },
    methods: {
      async fetchReviewData() {
        try {
          const response = await axios.get(`api/review/${this.bid}`);
                  this.form = { ...this.form, ...response.data }; 
        } catch (error) {
          console.error('Error fetching review data:', error);
        }
      },
      async submitForm() {
        try {
          await axios.post(`api/review/tijiao2/${this.bid}`, this.form2);
          await axios.post(`api/jindu/gaibian2/${this.bid}`, this.forms3);
          this.$message.success("提交成功");
          // 可以在此处添加成功提交后的逻辑，例如显示通知或清空表单
          // 将数据作为参数传递到 "/reviewList"
         this.$router.push({ path: '/reviewList', query: { isTrue: true } })
        } catch (error) {
          console.error('提交失败:', error);
          this.$message.error("提交失败，请重试");
          // 可以在此处处理错误，例如显示错误通知
        }
      },
      async rejectForm() {
        try {
          await axios.post(`api/review/dahui/${this.bid}`);
          await axios.post(`api/jindu/gaibian2/${this.bid}`, this.forms4);
          this.$message.success("打回成功");
          this.$router.push('/reviewList');
        }catch (error) {
          console.error('打回失败:', error);
          this.$message.error("打回失败，请重试");
          // 可以在此处处理错误，例如显示错误通知
        }
      },
      showNotifications() {
        this.form10.lastNotificationCount = this.notifications.length; // 更新上次数量
        axios.post('/api/project/update2',this.form10); // 发送请求传数据
        this.$router.push("/teachersnotice");
      },
      async fetchNotifications() {
        try {
          const response = await axios.get('/api/project/length'); // 从后端获取通知
          this.notifications = response.data; // 假设返回的数据是通知数组
          const response2= await axios.get('/api/project/count'); // 从后端获取通知列表
          this.form10.lastNotificationCount = response2.data; // 返回的数据是通知数量
        } catch (error) {
          console.error("获取通知失败:", error);
        }
      },
      //下载附件
      async downloadAttachment() {
      try {
        // 调用后端接口获取文件信息
        const response = await axios.get(
          `/api/declaration/download/${this.bid}`,
          { responseType: "blob" }
        );
        // 获取文件名和文件路径
        const fileNameResponse = await axios.get(
          `/api/declaration/getFileName/${this.bid}`
        );
        const fileName = fileNameResponse.data.fileName;
        // 创建 Blob 对象和 URL
        const url = window.URL.createObjectURL(new Blob([response.data]));
        const a = document.createElement("a"); // 创建一个链接元素
        a.href = url; // 设置链接的 href 属性
        a.download = fileName; // 设置下载文件的名称
        document.body.appendChild(a); // 将链接添加到 DOM 中
        a.click(); // 触发点击事件，开始下载
        a.remove(); // 下载完成后移除链接元素
        window.URL.revokeObjectURL(url); // 释放内存
      } catch (error) {
        console.error("下载附件失败:", error);
        this.$message.error("下载失败，请重试");
      }
    },
    },
    mounted() {
      this.fetchReviewData(); 
      this.fetchNotifications(); // 组件挂载时获取通知
  
    },
  };
  </script>
  
  <style scoped>
  #app {
    font-family: Avenir, Helvetica, Arial, sans-serif;
    color: #2c3e50;
    height: 92vh;
  }
  .notification-dot {
    position: absolute;
    top: -5px;
    right: -5px;
    width: 10px;
    height: 10px;
    background-color: red;
    border-radius: 50%;
  }
  .el-aside a {
    text-decoration: none;
    color: #2c3e50;
  }
  .el-container {
    display: flex;
    height: 100%;
  }
  .el-header {
    background-color: #4caf50;
    color: white;
  }
  .el-table {
    margin-top: 20px;
  }
  .green-background {
    background-color: green;
    color: white; /* 可选，调整文字颜色 */
  }
  .form-container {
    max-height: 540px; /* 设置最大高度 */
    overflow-y: auto; /* 纵向滚动条 */
    padding: 20px;
    background: #fff; /* 背景颜色 */
    border-radius: 5px; /* 圆角 */
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1); /* 阴影 */
  }
  </style>