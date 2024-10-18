<template>
  <div id="app">
    <el-container style="overflow: hidden">
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
        <el-main style="overflow-y: auto">
          <div>
            <h2>老师工作台</h2>
            <div class="project-statistics-container">
              <div
                class="statistic-box"
                :class="userStatusClass"
                @click="goToNotifications"
              >
                <strong>查看通知列表</strong>
              </div>
              <div class="statistic-box" @click="goToDeclare">
                <strong>查看申报列表</strong>
              </div>
            </div>
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
import axios from "axios"; // 引入 Axios

export default {
  data() {
    return {
      tabPosition: "left",
      activeMenu: "",
      notifications: [],
      form10: {
        lastNotificationCount: 0,
      },
      userStatus: "active", // 用户状态，可以是 'active', 'inactive', 'warning'

    };
  },
  computed: {
    showNotificationDot() {
      return this.notifications.length > this.form10.lastNotificationCount;
    },
    userStatusClass() {
      return {
        'green-background': this.userStatus === 'active',
        'yellow-background': this.userStatus === 'warning',
        'red-background': this.userStatus === 'inactive',
      }
    },
  },
  methods: {
    showNotifications() {
      this.form10.lastNotificationCount = this.notifications.length;
      axios.post("/api/project/update2", this.form10);
      this.$router.push("/teachersnotice");
    },
    goToNotifications() {
      // 跳转到通知列表页面
      this.showNotifications(); // 可选：也可以在这里调用显示通知的逻辑
    },
    goToDeclare() {
      // 跳转到申报列表页面
      this.$router.push("/teacherdeclare");
    },
    async fetchNotifications() {
      try {
        const response = await axios.get("/api/project/length");
        this.notifications = response.data;
        const response2 = await axios.get("/api/project/count");
        this.form10.lastNotificationCount = response2.data;
      } catch (error) {
        console.error("获取通知失败:", error);
      }
    },
  },
  mounted() {
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
.yellow-background {
  background-color: yellow;
  color: black; /* 可选，调整文字颜色 */
}
.red-background {
  background-color: red;
  color: white; /* 可选，调整文字颜色 */
}
.project-statistics-container {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
}
.statistic-box {
  border: 1px solid #4caf50;
  border-radius: 8px;
  padding: 20px;
  flex: 1;
  margin: 0 10px; /* 添加间距 */
  text-align: center;
  cursor: pointer;
  transition: background-color 0.3s;
}
</style>