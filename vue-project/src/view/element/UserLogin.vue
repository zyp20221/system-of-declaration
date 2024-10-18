<template>
  <div class="login-container">
    <el-card class="login-card">
      <h1 class="title">欢迎登录</h1>
      <el-tabs v-model="activeTab" @tab-click="handleClick">
        <el-tab-pane label="学生" name="student">
          <el-form ref="loginForm" :model="loginForm" label-width="80px">
            <el-form-item label="学号">
              <el-input v-model="loginForm.gxh"></el-input>
            </el-form-item>
            <el-form-item label="密码">
              <el-input type="password" v-model="loginForm.pwd"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="studentsLogin()">登录</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="老师" name="teacher">
          <el-form ref="loginForm" :model="loginForm" label-width="80px">
            <el-form-item label="工号">
              <el-input v-model="loginForm.gxh"></el-input>
            </el-form-item>
            <el-form-item label="密码">
              <el-input type="password" v-model="loginForm.pwd"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="teachersLogin()">登录</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="管理员" name="admin">
          <el-form ref="loginForm" :model="loginForm" label-width="80px">
            <el-form-item label="工号">
              <el-input v-model="loginForm.gxh"></el-input>
            </el-form-item>
            <el-form-item label="密码">
              <el-input type="password" v-model="loginForm.pwd"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="adminsLogin()">登录</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      activeTab: "student",
      loginForm: {
        gxh: "",
        pwd: "",
        role: "student",
      },
    };
  },
  methods: {
    handleClick(tab) {
      this.loginForm.role = tab.name;
      this.loginForm.gxh = ""; // 清空工号/学号
      this.loginForm.pwd = ""; // 清空密码
    },
    async studentsLogin() {
      await this.login('api/users/login');
    },
    async teachersLogin() {
      await this.login('api/users/login2');
    },
    async adminsLogin() {
      await this.login('api/users/login3');
    },
    async login(url) {
      try {
        const response = await axios.post(url, {
          gxh: this.loginForm.gxh,
          pwd: this.loginForm.pwd,
        });
        const { token } = response.data;

        // 处理 token，例如存储在本地存储
        localStorage.setItem('token', token);
        // 设置 Axios 默认请求头
        axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
        // 根据角色跳转
        if (this.loginForm.role === 'student') {
          this.$router.push("/students");
        } else if (this.loginForm.role === 'teacher') {
          this.$router.push("/teachers");
        } else {
          this.$router.push("/administrator");
        }
        this.$message.success("登录成功");
      } catch (error) {
        console.error("登录失败", error);
        this.$message.error("登录失败，请检查用户名和密码");
      }
    },
  },
};
</script>

<style scoped>
.el-button {
  margin-left: 60px;
}
.register-link {
  text-align: center;
  margin-top: 20px;
}
a {
  text-decoration: none;
  color: #409eff;
  cursor: pointer;
  font-size: 14px;
}
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 95vh;
  background-size: cover;
}

.login-card {
  width: 400px;
  border-radius: 15px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.title {
  text-align: center;
  margin-bottom: 30px;
}
</style>