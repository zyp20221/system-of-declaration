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
          <el-row>
            <el-col :span="24">
              <h2>老师管理</h2>
            </el-col>
            <div style="margin-bottom: 20px">
              <el-input
                placeholder="输入老师名称"
                v-model="userName"
                style="width: 200px; margin-right: 10px"
              ></el-input>
              <el-button
                @click="filterData"
                type="primary"
                style="margin-left: 10px"
                >搜索</el-button
              >
              <el-button @click="zhiding()" style="margin-left: 420px"
                >指定申报书</el-button
              >
            </div>
          </el-row>
          <el-table
            :data="paginatedData"
            style="width: 100%"
            :row-class-name="getRowClassName"
            @select="handleSelect"
            @select-all="handleSelectAll"
            v-model="selectedProjects"
          >
            <el-table-column type="selection" width="55"></el-table-column>
            <el-table-column
                    prop="uid"
                    label="用户ID"
                    width="100"
                  ></el-table-column>
                  <el-table-column
                    prop="username"
                    label="用户名"
                    width="200"
                  ></el-table-column>
                  <el-table-column label="操作" width="480">
                    <template v-slot="scope">
                      <el-button @click="edit(scope.row.uid)">编辑</el-button>
                      <el-button type="danger" @click="deleteItem(scope.row.uid)">删除</el-button>
                    </template>
                  </el-table-column>
          </el-table>

          <el-pagination
            background
            layout="total, prev, pager, next"
            :total="filteredData.length"
            :page-size="pageSize"
            :current-page="currentPage"
            @current-change="handlePageChange"
          ></el-pagination>
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
      tableData: [],
      filteredData: [],
      currentPage: 1,
      pageSize: 5,
      selectedProjects: [],
      userName: "",
      identify: 2, 
    };
  },
  computed: {
    paginatedData() {
      const start = (this.currentPage - 1) * this.pageSize;
      return this.filteredData.slice(start, start + this.pageSize);
    },
  },
  methods: {
    async fetchData() {
        try {
          const response = await axios.get(`/api/project/users/${this.identify}`);
          this.tableData = response.data; // 假设后端返回的用户数据在data字段中
          this.filteredData = this.tableData; // 初始化 filteredData
        } catch (error) {
          console.error("获取用户数据失败:", error);
        }
      },
      handlePageChange(newPage) {
        this.currentPage = newPage;
      },
      addUser() {
        // 这里可以打开一个弹窗或跳转到新增用户的页面
        this.$router.push("/register");
      },
      edit(uid){
        // 这里可以打开一个弹窗或跳转到编辑用户的页面
        this.$router.push({ path: "/edit", query: { uid:uid } });
    },
      async deleteItem(uid) {
        try {
          await axios.delete(`/api/project/delete/${uid}`);
          this.$message.success("用户删除成功");
          this.fetchData(); // 重新获取用户数据
        } catch (error) {
          console.error("删除用户失败:", error);
          this.$message.error("删除用户失败，请重试");
        }
      },
    handleSelect(selection) {
      this.selectedProjects = selection.map((item) => item.uid);
    },
    handleSelectAll(selection) {
      this.selectedProjects = selection.map((item) => item.uid);
    },
    zhiding() {
      if (this.selectedProjects.length === 0) {
        this.$message.warning("请选择至少一个老师");
        return;
      }
      this.$router.push({
        path: "/alldeclaration",
        query: {
          ids: this.selectedProjects.join(','),
        },
      });
    },
    filterData() {
      this.filteredData = this.tableData.filter((item) => {
        const matchesName = item.username.includes(this.userName);
        return matchesName;
      });
      this.currentPage = 1; // Reset to the first page after filtering
    },
  },
  mounted() {
    this.fetchData();
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
.green-background {
  background-color: green;
  color: white; /* 可选，调整文字颜色 */
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
</style>