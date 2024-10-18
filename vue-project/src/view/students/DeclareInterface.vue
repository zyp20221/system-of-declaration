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
        <el-main>
          <div class="form-container">
            <el-form :model="form" label-width="100px">
              <el-form-item label="项目年份:">
                <el-input
                  v-model="form.projectYear"
                  :disabled="form.projectYear !== ''"
                  placeholder="项目年份"
                ></el-input>
              </el-form-item>
              <el-form-item label="项目名称:">
                <el-input
                  v-model="form.projectName"
                  :disabled="form.projectName !== ''"
                  placeholder="项目名称"
                ></el-input>
              </el-form-item>
              <el-form-item label="项目类型:">
                <el-input
                  v-model="form.projectType"
                  :disabled="form.projectType !== ''"
                  placeholder="项目类型"
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
                <div style="margin-top: 5px; color: #909399">
                  剩余字数: {{ 500 - form.projectBrief.length }}
                </div>
              </el-form-item>
              <el-form-item label="项目成员:">
                <el-input
                  v-model="newMember"
                  placeholder="请输入成员姓名"
                  style="width: 300px"
                ></el-input>
                <el-button type="primary" @click="addMember"
                  >添加成员</el-button
                >
              </el-form-item>
              <el-form-item label="成员列表:">
                <el-table :data="members" style="width: 100%">
                  <el-table-column prop="name" label="姓名" />
                  <el-table-column label="操作">
                    <template #default="scope">
                      <el-button type="danger" @click="removeMember(scope.row)"
                        >删除</el-button
                      >
                    </template>
                  </el-table-column>
                </el-table>
              </el-form-item>
              <el-form-item label="指导老师:">
                <el-table :data="teachers" style="width: 100%">
                  <el-table-column prop="name" label="老师姓名" />
                  <el-table-column label="操作">
                    <template #default="scope">
                      <el-button type="danger" @click="removeTeacher(scope.row)"
                        >删除</el-button
                      >
                    </template>
                  </el-table-column>
                </el-table>
                <el-input
                  v-model="newTeacher"
                  placeholder="请输入老师姓名"
                  style="width: 300px"
                />
                <el-button type="primary" @click="addTeacher"
                  >添加老师</el-button
                >
              </el-form-item>
              <el-form-item label="申报内容">
                <el-input
                  type="textarea"
                  v-model="form.declarationContent"
                  :rows="4"
                  placeholder="请输入申报内容"
                ></el-input>
              </el-form-item>
              <el-form-item label="附件上传">
                <input type="file" @change="onFileChange" />
                <button @click="(event) => uploadFile(event)">上传文件</button>
              </el-form-item>
              <el-form-item>
                <div style="display: flex; justify-content: center; gap: 20px">
                  <el-button type="primary" @click="submitForm1"
                    >提交评审</el-button
                  >
                  <el-button type="primary" @click="submitForm2"
                    >保存并返回列表</el-button
                  >
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
import axios from "axios";
export default {
  data() {
    return {
      projectCode: null,
      form: {
        projectBrief: "",
        projectMember: "",
        adviserId: "",
        declarationContent: "",
        projectYear: "",
        projectName: "",
        projectType: "",
        declarationName: "",
        projectId: this.$route.query.id,
        fileName:"",
        filePath:"",
      },
      newMember: "",
      newTeacher: "",
      members: [],
      teachers: [],
      forms: {
        studentsJindu: 1,
        teacherJindu: 0,
        zhuanjiaJindu: 0,
        schoolJindu: 0,
        collegeJindu: 0,
        fabuJindu: 0,
        projectName: "",
      },
      forms2: {
        studentsJindu: 0,
        teacherJindu: 0,
        zhuanjiaJindu: 0,
        schoolJindu: 0,
        collegeJindu: 0,
        fabuJindu: 0,
        projectName: "",
      },
      form3: {
        projectYear: "",
        projectName: "",
        projectType: "",
      },
      pId: this.$route.query.pId,
      notifications: [],
      form10: {
        lastNotificationCount: 0,
      },
      fileList: [], // 上传附件列表
      selectedFile: null,
    };
  },
  computed: {
    showNotificationDot() {
      // 只有当通知数量大于上次通知数量时才显示红点
      return this.notifications.length > this.form10.lastNotificationCount; // 只有当数量增加时显示红点
    },
  },
  methods: {
    addMember() {
      if (this.newMember.trim() === "") {
        this.$message.warning("请输入成员姓名");
        return;
      }
      this.members.push({ name: this.newMember });
      this.newMember = "";
    },
    removeMember(member) {
      this.members = this.members.filter((m) => m !== member);
    },
    async addTeacher() {
      if (this.newTeacher.trim() === "") {
        this.$message.warning("请输入老师姓名");
        return;
      }
      try {
        // 获取老师的 uid
        const response = await axios.get("/api/declaration", {
          params: {
            adviserName: this.newTeacher,
          },
        });
        if (response.data) {
          this.form.adviserId = response.data; // 将获取到的 uid 赋值给 adviserId
          this.teachers.push({ name: this.newTeacher });
          this.newTeacher = "";
          this.$message.success("添加老师成功");
        } else {
          this.$message.warning("未找到该老师");
        }
      } catch (error) {
        console.error(error);
        this.$message.error("添加老师失败，请重试");
      }
    },
    removeTeacher(teacher) {
      this.teachers = this.teachers.filter((t) => t !== teacher);
      this.form.adviserId = ""; // 清空 adviserId
    },
    async submitForm1() {
      this.form.projectMember = this.members.map((m) => m.name).join(", ");
      this.forms.projectName = this.form.declarationName;
      try {
        await axios.post("api/declaration/tijiao", this.form);
        if (this.pId) {
          await axios.post(`/api/jindu/${this.pId}`, this.forms);
        } else {
          await axios.post(`/api/jindu`, this.forms);
        }
        this.$message.success("提交成功");
        this.form = {};
        this.members = [];
        this.teachers = [];
        this.$router.push("/declare");
      } catch (error) {
        console.error(error);
        this.$message.error("提交失败，请重试");
      }
    },
    async submitForm2() {
      this.form.projectMember = this.members.map((m) => m.name).join(", ");
      this.forms2.projectName = this.form.declarationName;
      try {
        await axios.post("api/declaration", this.form);
        if (this.pId) {
          await axios.post(`/api/jindu/${this.pId}`, this.forms2);
        } else {
          await axios.post(`/api/jindu`, this.forms2);
        }
        this.$message.success("保存成功");
        this.form = {};
        this.members = [];
        this.teachers = [];
        this.$router.push({
          path: "/declare",
          query: { id: this.form.projectId },
        });
      } catch (error) {
        console.error(error);
        this.$message.error("保存失败，请重试");
      }
    },
    passParameterToProgress() {
      // 更新路由参数，但不跳转
      this.$router.replace({ path: "/progress", query: { completed: true } });
    },
    showNotifications() {
      this.form10.lastNotificationCount = this.notifications.length; // 更新上次数量
      axios.post("/api/project/update2", this.form10); // 发送请求传数据
      this.$router.push("/studentsnotice");
    },
    async fetchNotifications() {
      try {
        const response = await axios.get("/api/project/length"); // 从后端获取通知
        this.notifications = response.data; // 假设返回的数据是通知数组
        const response2 = await axios.get("/api/project/count"); // 从后端获取通知列表
        this.form10.lastNotificationCount = response2.data; // 返回的数据是通知数量
      } catch (error) {
        console.error("获取通知失败:", error);
      }
    },
    onFileChange(event) {
      this.selectedFile = event.target.files[0];
      console.log(this.selectedFile);
    },
    async uploadFile(event) {
      // 阻止默认行为
      event.preventDefault();

      if (!this.selectedFile) {
        this.message = "请选择一个文件";
        return;
      }

      const formData = new FormData();
      formData.append("file", this.selectedFile);
      try {
        const response = await axios.post("/api/declaration/upload", formData, {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        });
        this.form.fileName = response.data.fileName;
        this.form.filePath = response.data.filePath;
        this.$message.success("文件上传成功");
      } catch (error) {
        this.message = "文件上传失败";
        console.error(error);
      }
    },
  },
  mounted() {
    // 从路由查询参数中获取 projectCode
    this.projectCode = this.$route.query.pId;
    // 你可以在这里执行其他逻辑，例如根据 projectCode 加载数据
    if (this.projectCode) {
      // 根据 projectCode 加载数据
      axios.get("/api/declaration/" + this.projectCode).then((response) => {
        this.form = response.data;
        this.members = this.form.projectMember.split(",").map((name) => ({
          name: name.trim(),
        }));

        // 获取老师姓名
        axios
          .get("/api/declaration/adviser/" + this.form.adviserId)
          .then((response) => {
            this.teachers = [{ name: response.data }];
          });
      });
    }
    //获取项目表中信息
    if (this.form.projectId) {
      axios.get(`/api/project/${this.form.projectId}`).then((response) => {
        this.form3 = response.data;
        this.form.projectYear = this.form3.projectYear;
        this.form.projectName = this.form3.projectName;
        this.form.projectType = this.form3.projectType;
      });
    }
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
.form-container {
  max-height: 540px; /* 设置最大高度 */
  overflow-y: auto; /* 纵向滚动条 */
  padding: 20px;
  background: #fff; /* 背景颜色 */
  border-radius: 5px; /* 圆角 */
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1); /* 阴影 */
}
</style>