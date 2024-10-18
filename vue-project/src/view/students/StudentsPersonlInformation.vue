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
        <el-main style="padding: 10px;">
          <h2 style="margin:0; padding: 0;">个人中心</h2>
          <el-row>
            <el-col>
              <el-card>
                <div style="text-align: center">
                  <el-avatar size="100" src="https://via.placeholder.com/100" />
                  <h3>{{ user.username }}</h3>
                  <p>学生</p>
                </div>
                <el-divider></el-divider>
                <el-tabs :tab-position="tabPosition" style="height: auto">
                  <el-tab-pane label="个人信息">
                    <el-col>
                      <el-card
                        style="
                          max-height: 300px;
                          overflow-y: auto;
                          width: 1000px;
                        "
                      >
                        <div>
                          <el-form :model="user" label-width="100px">
                            <el-form-item label="用户名">
                              <el-input v-model="user.username"></el-input>
                            </el-form-item>
                            <el-form-item label="学号">
                              <el-input v-model="user.gxh"></el-input>
                            </el-form-item>
                            <el-form-item label="电子邮箱">
                              <el-input v-model="user.email"></el-input>
                            </el-form-item>
                            <el-form-item label="手机">
                              <el-input v-model="user.phoneNumber"></el-input>
                            </el-form-item>
                          </el-form>
                          <el-button
                            @click="baocun"
                            type="primary"
                            style="margin-top: 60px; margin-left: 400px"
                            >保存</el-button
                          >
                        </div>
                      </el-card>
                    </el-col>
                  </el-tab-pane>
                  <el-tab-pane label="修改密码">
                    <el-col>
                      <el-card
                        style="
                          max-height: 300px;
                          overflow-y: auto;
                          width: 1000px;
                        "
                      >
                        <el-form :model="form" label-width="100px">
                          <el-form-item label="原密码">
                            <el-input
                              :type="showOldPassword ? 'text' : 'password'"
                              v-model="form.oldPassword"
                            >
                              <template #append>
                                <el-button
                                  @click="toggleOldPassword"
                                  icon="el-icon-view"
                                ></el-button>
                              </template>
                            </el-input>
                          </el-form-item>
                          <el-form-item label="新密码">
                            <el-input
                              :type="showNewPassword ? 'text' : 'password'"
                              v-model="form.newPassword"
                            >
                              <template #append>
                                <el-button
                                  @click="toggleNewPassword"
                                  icon="el-icon-view"
                                ></el-button>
                              </template>
                            </el-input>
                          </el-form-item>
                          <el-form-item label="确认新密码">
                            <el-input
                              :type="showConfirmPassword ? 'text' : 'password'"
                              v-model="form.confirmPassword"
                            >
                              <template #append>
                                <el-button
                                  @click="toggleConfirmPassword"
                                  icon="el-icon-view"
                                ></el-button>
                              </template>
                            </el-input>
                          </el-form-item>
                          <el-form-item>
                            <el-button
                              type="primary"
                              @click="save"
                              style="margin-top: 60px; margin-left: 400px"
                              >保存</el-button
                            >
                          </el-form-item>
                        </el-form>
                      </el-card>
                    </el-col>
                  </el-tab-pane>
                </el-tabs>
              </el-card>
            </el-col>
          </el-row>
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
import axios from "axios"; // 导入 axios

export default {
  data() {
    return {
      tabPosition: "left",
      user: {
        username: "",
        gxh: "",
        email: "",
        phoneNumber: "",
        pwd: "",
      },
      form: {
        oldPassword: "",
        newPassword: "",
        confirmPassword: "",
      },
      showOldPassword: false,
      showNewPassword: false,
      showConfirmPassword: false,
      notifications: [],
      form10:{
        lastNotificationCount: 0,
      }
    };
  },
  computed: {
    showNotificationDot() {
      // 只有当通知数量大于上次通知数量时才显示红点
      return this.notifications.length > this.form10.lastNotificationCount; // 只有当数量增加时显示红点
    },
  },
  mounted() {
    this.fetchUserData(); // 页面加载时获取用户数据
    this.fetchNotifications(); // 组件挂载时获取通知
  },
  methods: {
    async fetchUserData() {
      try {
        const response = await axios.get("api/users"); // 发起 GET 请求
        this.user = response.data; // 将返回的数据赋值给 user
      } catch (error) {
        console.error("获取用户数据失败:", error);
      }
    },
    async baocun() {
      try {
        const response = await axios.post("api/users/save", this.user); // 发起 POST 请求
        if (response.status === 200) {
          this.$message.success("用户信息保存成功!"); // 提示成功信息
          this.$router.push("/students"); // 跳转到主页面
        }
      } catch (error) {
        console.error("保存用户信息失败:", error);
        this.$message.error("保存用户信息失败!"); // 提示失败信息
      }
    },
    async save() {
      if (this.form.newPassword !== this.form.confirmPassword) {
        this.$message.error("新密码与确认密码不一致!");
        return;
      }
      const currentPassword = this.user.pwd; // 确保在用户数据中有这个字段
      if (this.form.oldPassword !== currentPassword) {
        this.$message.error("原密码不正确!");
        return;
      }

      try {
        const response = await axios.post(
          "api/users/savepwd",
          this.form, // 发送修改密码的请求
        );
        if (response.status === 200) {
          this.$message.success("密码修改成功!");
          this.form = {};
          this.$router.push("/students");
        }
      } catch (error) {
        console.error("保存密码失败:", error);
        this.$message.error("保存密码失败!");
      }
    },
    toggleOldPassword() {
      this.showOldPassword = !this.showOldPassword;
    },
    toggleNewPassword() {
      this.showNewPassword = !this.showNewPassword;
    },
    toggleConfirmPassword() {
      this.showConfirmPassword = !this.showConfirmPassword;
    },
    showNotifications() {
      this.form10.lastNotificationCount = this.notifications.length; // 更新上次数量
      axios.post('/api/project/update2',this.form10); // 发送请求传数据
      this.$router.push("/studentsnotice");
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

.el-card {
  margin-bottom: 20px;
}
</style>