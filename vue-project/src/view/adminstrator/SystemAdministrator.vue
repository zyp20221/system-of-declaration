<template>
  <div id="app">
    <el-container style="overflow: hidden">
      <el-header
        style="
          background-color: #4caf50;
          color: white;
          display: flex;
          justify-content: center;
          align-items: center;
          height: 60px;
        "
      >
        <span style="font-size: 24px">申报与评审系统</span>
        <router-link to="/login" style="margin-left: auto;">
          <el-avatar
            size="40"
            src="https://via.placeholder.com/100"
            alt="个人图像"
          />
        </router-link>
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
        <el-main style="overflow-y: auto">
          <div style="padding: 20px;">
            <h2>项目统计</h2>
            <div class="project-statistics-container">
              <div class="statistic-box" @click="navigateToManagement">
                <strong>已发布项目数：</strong> {{ publishedProjects }}
              </div>
              <div class="statistic-box" @click="navigateToManagement2">
                <strong>进行中项目数：</strong> {{ ongoingProjects }}
              </div>
              <div class="statistic-box" @click="navigateToManagement3">
                <strong>停止的项目数：</strong> {{ stopedProjects }}
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
import axios from 'axios';

export default {
  data() {
    return {
      tabPosition: "left",
      activeMenu: "",
      publishedProjects: 0, // 初始化为0
      ongoingProjects: 0, // 初始化为0
      stopedProjects: 0 // 初始化为0
    };
  },
  mounted() {
    this.fetchProjectNumbers(); // 组件挂载后获取数据
  },
  methods: {
    fetchProjectNumbers() {
      axios.get('/api/project/numbers')
        .then(response => {
          const { published, ongoing, stopped } = response.data;
          this.publishedProjects = published;
          this.ongoingProjects = ongoing;
          this.stopedProjects = stopped;
        })
        .catch(error => {
          console.error("无法获取项目数据:", error);
        });
    },
    navigateToManagement() {
      this.$router.push('/manage'); // 跳转到项目管理页面
    },
    navigateToManagement2() {
      this.$router.push({path: '/manage', query: {status:1}}); // 跳转到项目管理页面
    },
    navigateToManagement3() {
      this.$router.push({path: '/manage', query: {status:2}}); // 跳转到项目管理页面
    },
  }
};
</script>

<style scoped>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  color: #2c3e50;
  height: 92vh;
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

.project-statistics-container {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
}

.statistic-box {
  background-color: #f0f8ff;
  border: 1px solid #4caf50;
  border-radius: 8px;
  padding: 20px;
  flex: 1;
  margin: 0 10px; /* 添加间距 */
  text-align: center;
  cursor: pointer;
  transition: background-color 0.3s;
}

.statistic-box:hover {
  background-color: #e0f7fa; /* 悬停时变色 */
}

.statistic-box strong {
  display: block;
  font-size: 18px;
  color: #333;
}
</style>