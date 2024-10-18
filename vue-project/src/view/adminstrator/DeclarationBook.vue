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
        <div style="display: flex; align-items: center">
          <el-button
            icon="el-icon-bell"
            @click="showNotifications"
            style="
              background-color: transparent;
              color: white;
              border: none;
              margin-right: 20px;
              font-size: 20px;
              position: relative;
            "
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
                  to="/administrator"
                  style="font-size: large; font-weight: bold"
                  >主页</router-link
                >
              </el-menu-item>
              <el-submenu index="2">
                <template #title>项目管理</template>
                <el-menu-item index="2-1">
                  <router-link to="/management">发布项目</router-link>
                </el-menu-item>
                <el-menu-item index="2-2">
                  <router-link to="/manage">项目管理</router-link>
                </el-menu-item>
              </el-submenu>
              <el-submenu index="3">
                <template #title>用户管理</template>
                <el-menu-item index="3-1">
                  <router-link to="/teachermanagement">老师管理</router-link>
                </el-menu-item>
                <el-menu-item index="3-2">
                  <router-link to="/studentsmanagement">学生管理</router-link>
                </el-menu-item>
              </el-submenu>
              <el-menu-item index="4">
                <router-link to="/adminpersonal">个人中心</router-link>
              </el-menu-item>
            </el-menu>
          </el-aside>
        <el-main>
          <h2>申报书详情</h2>
          <div class="form-container">
            <el-form :model="form" label-width="100px">
              <el-form-item label="项目名称:">
                <el-input
                  v-model="form.projectName"
                  placeholder="项目名称"
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
import axios from "axios";

export default {
  data() {
    return {
      form: {
        projectName: "",
        projectBrief: "",
        declarationContent: "",
      },
      bid: this.$route.query.bid,
    };
  },
  methods: {
    async fetchReviewData() {
      try {
        const response = await axios.get(`api/review/${this.bid}`);
        this.form = { ...this.form, ...response.data };
      } catch (error) {
        console.error("Error fetching review data:", error);
      }
    },
  },
  mounted() {
    this.fetchReviewData();
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