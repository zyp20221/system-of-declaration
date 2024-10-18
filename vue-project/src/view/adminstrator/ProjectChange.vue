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
        <router-link to="/login" style="margin-left: auto">
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
          <h2 style="text-align: left; margin: 0">修改项目</h2>
          <div class="form-container">
            <el-form :model="form" label-width="120px">
              <el-form-item label="项目名称">
                <el-input
                  v-model="form.projectName"
                  placeholder="请输入项目名称"
                ></el-input>
              </el-form-item>
              <el-form-item label="项目类型">
                <el-input
                  v-model="form.projectType"
                  placeholder="请输入项目类型"
                ></el-input>
              </el-form-item>
              <el-form-item label="项目年份">
                <el-input
                  v-model="form.projectYear"
                  placeholder="请输入项目年份"
                ></el-input>
              </el-form-item>
              <el-form-item label="结束日期">
                <el-date-picker
                  v-model="form.endTime"
                  type="date"
                  placeholder="选择结束日期"
                ></el-date-picker>
              </el-form-item>
              <el-form-item label="项目通知(500字以内):">
                <el-input
                  type="textarea"
                  v-model="form.projectNotice"
                  :rows="4"
                  placeholder="请输入项目通知"
                ></el-input>
                <div style="margin-top: 5px; color: #909399">
                  剩余字数: {{ 500 - form.projectNotice.length }}
                </div>
              </el-form-item>
              <el-form-item label="项目说明(500字以内):">
                <el-input
                  type="textarea"
                  v-model="form.projectIntroduce"
                  :rows="4"
                  placeholder="请输入项目说明"
                ></el-input>
                <div style="margin-top: 5px; color: #909399">
                  剩余字数: {{ 500 - form.projectIntroduce.length }}
                </div>
              </el-form-item>
              <el-form-item label="身份限制">
                <el-select
                  v-model="form.identifyRestriction" 
                  placeholder="请选择该项目的身份限制"
                >
                  <el-option label="学生" value="3"></el-option>
                  <el-option label="教师" value="2"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-button
                  type="primary"
                  @click="submitForm"
                  style="margin-left: 45px"
                  >修改项目</el-button
                >
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
      tabPosition: "left",
      activeMenu: "",
      form: {
        projectName: "",
        projectType: "",
        projectYear: "",
        projectNotice: "",
        projectIntroduce: "",
        identifyRestriction: "",
        endTime: "", // 添加结束日期字段
      },
      projectId: this.$route.query.projectId,
      form2: {},
    };
  },
  mounted() {
    this.fetchProjectData();
  },
  methods: {
    async fetchProjectData() {
      try {
        const response = await axios.get(
          `/api/project/getall/${this.projectId}`
        );
        this.form = response.data; // 直接将返回的数据赋值给 form

        // 修正 identifyRestriction 的赋值
        this.form.identifyRestriction = response.data.identifyRestriction === 3 ? "学生" : "教师";
      } catch (error) {
        this.$message.error("获取项目数据失败");
      }
    },
    async submitForm() {
      try {
        // 修正 identifyRestriction 的赋值
        this.form.identifyRestriction = this.form.identifyRestriction === "学生" ? 3 : 2;
        // 将 endTime 加一天
        const selectedDate = new Date(this.form.endTime);
        selectedDate.setDate(selectedDate.getDate() + 1);
        await axios.post("/api/project", this.form);
        this.$message.success("修改成功");
        this.$router.push("/manage");
      } catch (error) {
        this.$message.error(
          "提交失败，请检查输入是否正确或者检查项目名是否重复"
        );
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
.el-table {
  margin-top: 20px;
}
.green-background {
  background-color: green;
  color: white; /* 可选，调整文字颜色 */
}
.form-container {
  max-width: 540px;
  margin: 20px auto;
  padding: 20px;
  background: #fff;
  border-radius: 5px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}
</style>