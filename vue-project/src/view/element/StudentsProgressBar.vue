<template>
    <div id="app">
      <el-container style="min-height: 100vh;">
        <el-header
          style="
            background-color: #4caf50;
            color: white;
            display: flex;
            justify-content: center;
            align-items: center;
          "
        >
          <span style="font-size: 24px">申报与评审系统</span>
          <router-link to="/login" style="margin-left: auto">
            <el-avatar
              size="40"
              src="https://via.placeholder.com/100"
              alt="个人图像"
            />
          </router-link>
        </el-header>
        <el-container style="flex: 1; display: flex;">
          <el-aside width="200px" style="background-color: #f5f5f5">
          <el-menu :default-active="activeMenu" class="el-menu-vertical-demo">
            <el-menu-item index="1">
              <router-link
                to="/students"
                style="font-size: large; font-weight: bold"
                >主页</router-link
              >
            </el-menu-item>
            <el-submenu index="2">
              <template #title>申报管理</template>
              <el-menu-item index="2-1">
                <router-link to="/declare">申报项目</router-link>
              </el-menu-item>
            </el-submenu>
            <el-menu-item index="3">
              <router-link to="/projectcheck">项目查看</router-link>
            </el-menu-item>
            <el-menu-item index="4">
              <router-link to="/personal">个人中心</router-link>
            </el-menu-item>
          </el-menu>
        </el-aside>
          <el-main style="overflow: auto; flex: 1; max-height: calc(100vh - 60px);">
            <div class="progress-container">
              <h2>项目状态&进度</h2>
              <div class="status-list">
                <div class="status-item" v-for="step in steps" :key="step.id">
                  <div class="step">{{ step.label }}</div>
                  <el-card :class="{'completed': step.completed, 'incomplete': !step.completed}" :body-style="{ padding: '20px' }">
                    <h3>{{ step.title }}</h3>
                    <el-button :type="step.completed ? 'success' : 'warning'">{{ step.completed ? '已完成' : '未完成' }}</el-button>
                  </el-card>
                  <div class="connector" v-if="step.id < steps.length"></div>
                </div>
              </div>
            </div>
          </el-main>
        </el-container>
        <el-footer style="background-color: #4caf50; color: white; text-align: center; padding: 10px;">
          用户单位：zyp 版权所有：zyp 访问量：0000
        </el-footer>
      </el-container>
    </div>
  </template>
  
  <script>
  import axios from 'axios';
  
  export default {
    data() {
      return {
        steps: [
          { id: 1, label: '步骤1', title: '学生申报项目', completed: 0 },
          { id: 2, label: '步骤2', title: '教师审核项目', completed: 0 },
          { id: 3, label: '步骤3', title: '学院专家审核项目', completed: 0 },
          { id: 4, label: '步骤4', title: '学院审核项目', completed: 0 },
          { id: 5, label: '步骤5', title: '学校申核项目', completed: 0 },
          { id: 6, label: '步骤6', title: '发布项目', completed: 0 },
        ],
        forms: {
          studentsJindu: "",
          teacherJindu: "",
          zhuanjiaJindu: "",
          schoolJindu: "",
          collegeJindu: "",
          fabuJindu: ""
        },
        pId:this.$route.query.pId,
      };
    },
    async mounted() {
      try {
        const response = await axios.get(`/api/jindu/${this.pId}`);
        this.forms = response.data; // Assuming the API response directly maps to forms
        this.steps[0].completed=this.forms.studentsJindu;
        this.steps[1].completed=this.forms.teacherJindu;
        this.steps[2].completed=this.forms.zhuanjiaJindu;
        this.steps[4].completed=this.forms.schoolJindu;
        this.steps[3].completed=this.forms.collegeJindu;
        this.steps[5].completed=this.forms.fabuJindu;
      } catch (error) {
        console.error("Error fetching data:", error);
        this.$message.error("数据加载失败，请重试");
      }
    }
  };
  </script>
  
  <style scoped>
  #app {
    font-family: Avenir, Helvetica, Arial, sans-serif;
    color: #2c3e50;
  }
  
  .el-aside a {
    text-decoration: none;
    color: #2c3e50;
  }
  
  .el-container {
    display: flex;
    min-height: 100vh; /* 确保最小高度为视口高度 */
  }
  
  .el-header {
    background-color: #4caf50;
    color: white;
  }
  
  .progress-container {
    text-align: left;
    padding: 20px;
  }
  
  .status-list {
    display: flex;
    flex-direction: column;
    align-items: center;
  }
  
  .status-item {
    position: relative;
    margin: 20px 0;
    width: 100%; /* 让每个步骤占满宽度 */
  }
  
  .step {
    font-weight: bold;
    margin-bottom: 10px;
  }
  
  .card {
    border-radius: 10px;
    width: 300px;
    text-align: center;
    transition: transform 0.3s;
  }
  
  .card.completed {
    background-color: #4caf50; /* 绿色背景 */
    color: white; /* 字体颜色 */
  }
  
  .card.incomplete {
    background-color: #f9c2c2; /* 红色背景 */
    color: #c0392b; /* 字体颜色 */
  }
  
  .card:hover {
    transform: scale(1.05);
  }
  
  .connector {
    width: 2px;
    height: 40px; /* 连接线的高度 */
    background-color: #4caf50; /* 连接线颜色 */
    position: absolute;
    left: 50%;
    top: 100%;
    transform: translateX(-50%);
  }
  </style>