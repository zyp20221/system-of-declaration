<template>
  <div class="login-container">
    <el-card class="login-card">
      <h1 class="title">欢迎注册</h1>
      <el-tabs v-model="activeTab" @tab-click="handleClick">
        <el-tab-pane label="申报人" name="applicant">
          <el-form ref="registerForm" :model="registerForm" label-width="80px">
            <el-form-item label="姓名" :rules="[{ required: true, message: '姓名不能为空', trigger: 'blur' }]">
              <el-input v-model="registerForm.username"></el-input>
            </el-form-item>
            <el-form-item label="学号" :rules="[{ required: true, message: '学号不能为空', trigger: 'blur' }]">
              <el-input v-model="registerForm.gxh"></el-input>
            </el-form-item>
            <el-form-item label="电话号码" :rules="[{ required: true, message: '电话号码不能为空', trigger: 'blur' }]">
              <el-input v-model="registerForm.phoneNumber"></el-input>
            </el-form-item>
            <el-form-item label="邮箱" :rules="[{ required: true, message: '邮箱不能为空', trigger: 'blur' }]">
              <el-input v-model="registerForm.email"></el-input>
            </el-form-item>
            <el-form-item label="密码" :rules="[{ required: true, message: '密码不能为空', trigger: 'blur' }]">
              <el-input type="password" v-model="registerForm.pwd"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="register()">注册</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="评审老师" name="reviewer">
          <el-form ref="registerForm" :model="registerForm" label-width="80px">
            <el-form-item label="姓名" :rules="[{ required: true, message: '姓名不能为空', trigger: 'blur' }]">
              <el-input v-model="registerForm.username"></el-input>
            </el-form-item>
            <el-form-item label="工号" :rules="[{ required: true, message: '工号不能为空', trigger: 'blur' }]">
              <el-input v-model="registerForm.gxh"></el-input>
            </el-form-item>
            <el-form-item label="电话号码" :rules="[{ required: true, message: '电话号码不能为空', trigger: 'blur' }]">
              <el-input v-model="registerForm.phoneNumber"></el-input>
            </el-form-item>
            <el-form-item label="邮箱" :rules="[{ required: true, message: '邮箱不能为空', trigger: 'blur' }]">
              <el-input v-model="registerForm.email"></el-input>
            </el-form-item>
            <el-form-item label="职称" :rules="[{ required: true, message: '职称不能为空', trigger: 'change' }]">
              <el-select v-model="registerForm.zhicheng" placeholder="请选择职称">
                <el-option label="教授" value="教授"></el-option>
                <el-option label="副教授" value="副教授"></el-option>
                <el-option label="讲师" value="讲师"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="密码" :rules="[{ required: true, message: '密码不能为空', trigger: 'blur' }]">
              <el-input type="password" v-model="registerForm.pwd"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="register()">注册</el-button>
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
      activeTab: "applicant",
      registerForm: {
        username: "",
        gxh: "",
        phoneNumber: "",
        email: "",
        zhicheng: "",
        pwd: "",
        identify: 3, // 3是申报人
      },
    };
  },
  methods: {
    handleClick(tab) {
      this.registerForm.identify = tab.name === 'reviewer' ? 2 : 3; // 更新身份
    },
    async register() {
      // 检查所有字段是否填写
      const isAllFieldsFilled = this.registerForm.username && this.registerForm.gxh &&
        this.registerForm.phoneNumber && this.registerForm.email && this.registerForm.pwd &&
        (this.registerForm.identify === 2 ? this.registerForm.zhicheng : true);

      if (!isAllFieldsFilled) {
        this.$message.error('请填写所有必填项！');
        return;
      }

      try {
        const response = await axios.post('api/users/register', {
          username: this.registerForm.username,
          gxh: this.registerForm.gxh,
          phoneNumber: this.registerForm.phoneNumber,
          email: this.registerForm.email,
          identify: this.registerForm.identify.toString(), // 转换为字符串
          zhicheng: this.registerForm.identify === 2 ? this.registerForm.zhicheng : null,
          pwd: this.registerForm.pwd,
        });

        // 检查后端返回的数据
        if (response.status === 200) {
          this.$message.success('注册成功，请登录！');
          this.$router.push('/usermanagement'); // 跳转到用户管理页面
        }
      } catch (error) {
        console.error('注册失败:', error);
        alert('注册失败，请重试！');
      }
    },
  },
};
</script>

<style scoped>
.el-button {
  margin-left: 60px;
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