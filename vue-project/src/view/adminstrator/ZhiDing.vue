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
              >主页</router-link>
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
          <h2>评审老师管理</h2>
          <el-button type="primary" @click="showAddDialog">新增评审老师</el-button>
          <el-table :data="reviewers" style="width: 100%" border>
            <el-table-column prop="declarationName" label="申报书名称" width="180"></el-table-column>
            <el-table-column prop="gongHao" label="工号" width="180"></el-table-column>
            <el-table-column prop="username" label="姓名" width="180"></el-table-column>
            <el-table-column label="操作" width="180">
              <template #default="scope">
                <el-button
                  @click="deleteReviewer(scope.row.declarationId, scope.row.gongHao)"
                  type="text"
                  size="large"
                  style="color: red"
                >删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-dialog title="新增评审老师" :visible.sync="addDialogVisible">
            <el-form :model="newReviewer" ref="addForm">
              <el-form-item
                label="工号"
                :rules="[{ required: true, message: '工号不能为空', trigger: 'blur' }]"
              >
                <el-input v-model="newReviewer.gongHao"></el-input>
              </el-form-item>
              <el-form-item
                label="姓名"
                :rules="[{ required: true, message: '姓名不能为空', trigger: 'blur' }]"
              >
                <el-input v-model="newReviewer.username"></el-input>
              </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
              <el-button @click="addDialogVisible = false">取 消</el-button>
              <el-button type="primary" @click="addReviewer">确 定</el-button>
            </span>
          </el-dialog>
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
      activeMenu: "",
      reviewers: [],
      newReviewer: { gongHao: "", username: "" },
      addDialogVisible: false,
      ids: this.$route.query.ids ? this.$route.query.ids.split(',') : [],
    };
  },
  mounted() {
    this.fetchReviewers();
  },
  methods: {
    async fetchReviewers() {
      try {
        const response = await axios.get("/api/pingshen", {
          params: { ids: this.ids.join(',') },
        });
        this.reviewers = response.data;
      } catch (error) {
        console.error("获取评审老师失败:", error);
        this.$message.error("获取评审老师失败，请稍后再试。");
      }
    },
    showAddDialog() {
      this.newReviewer = { gongHao: "", username: "" };
      this.addDialogVisible = true;
    },
    async addReviewer() {
      try {
        const requestData = {
          ids: this.ids,
          reviewer: this.newReviewer,
        };
        const response = await axios.post("/api/pingshen", requestData);
        this.reviewers.push(response.data);
        this.addDialogVisible = false;
        this.newReviewer = { gongHao: "", username: "" };
        this.fetchReviewers();
        this.$message.success("评审老师添加成功！");
      } catch (error) {
        console.error("添加评审老师失败:", error);
        this.$message.error("添加评审老师失败，请稍后再试。");
      }
    },
    async deleteReviewer(declarationId, gongHao) {
      try {
        await axios.delete(`/api/pingshen/${declarationId}/${gongHao}`);
        this.reviewers = this.reviewers.filter(reviewer => reviewer.gongHao !== gongHao);
        this.$message.success("评审老师删除成功！");
      } catch (error) {
        console.error("删除评审老师失败:", error);
        this.$message.error("删除评审老师失败，请稍后再试。");
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
</style>