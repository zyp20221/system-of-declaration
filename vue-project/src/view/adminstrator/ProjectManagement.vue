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
          <h2>项目管理</h2>
          <div style="margin-bottom: 20px">
            <el-input
              placeholder="输入项目名称"
              v-model="searchProjectName"
              style="width: 200px; margin-right: 10px"
            ></el-input>
            <el-select
              v-model="searchProjectType"
              placeholder="选择项目类型"
              style="width: 200px"
            >
              <el-option label="创新训练" value="创新训练"></el-option>
              <el-option label="创业训练" value="创业训练"></el-option>
            </el-select>
            <el-button
              @click="filterData"
              type="primary"
              style="margin-left: 10px"
              >搜索</el-button
            >
            <el-button
              @click="zhiding()"
              type="danger"
              style="margin-left: 420px"
              >批量删除指定项目</el-button
            >
          </div>
          <el-table
            :data="paginatedData"
            style="width: 100%"
            @select="handleSelect"
            @select-all="handleSelectAll"
            v-model="selectedProjects"
          >
            <el-table-column type="selection" width="55"></el-table-column>
            <el-table-column
              prop="projectName"
              label="项目名称"
              width="180"
            ></el-table-column>
            <el-table-column prop="projectType" label="项目类型" width="100">
            </el-table-column>
            <el-table-column prop="projectNumbers" label="申请数量" width="100">
              <template #default="{ row }">
                <router-link
                  :to="{ path: '/all', query: { projectId: row.pid } }"
                  class="plain-link"
                >
                  {{ row.projectNumbers }}
                </router-link>
              </template>
            </el-table-column>
            <el-table-column prop="createTime" label="创建时间" width="100">
              <template #default="{ row }">
                <span>{{ formatDate(row.createTime) }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="publishTime" label="发布时间" width="100">
              <template #default="{ row }">
                <span>{{ formatDate(row.publishTime) }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="updateTime" label="修改时间" width="100">
              <template #default="{ row }">
                <span>{{ formatDate(row.updateTime) }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="endTime" label="结束时间" width="100">
              <template #default="{ row }">
                <span>{{ formatDate(row.endTime) }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="projectStatus" label="项目状态" width="140">
              <template #default="{ row }">
                <span>{{ formatStatus(row.projectStatus) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="240">
              <template #default="{ row }">
                <el-button type="success" @click="changeStatus(row, 1)"
                  >接受申报</el-button
                >
                <el-button type="warning" @click="changeStatus(row, 2)"
                  >结束申报</el-button
                >
                <el-button
                  type="info"
                  @click="xiugai(row.pid)"
                  style="margin-left: -1px"
                  >修改项目</el-button
                >
                <el-button type="danger" @click="delete1(row.pid)"
                  >删除项目</el-button
                >
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
      activeMenu: "",
      tableData: [],
      filteredData: [],
      currentPage: 1,
      pageSize: 10,
      searchProjectName: "",
      searchProjectType: "",
      form: {
        projectName: "",
        projectStatus: "",
        projectType: "",
        projectYear: "",
        createTime: "",
        updateTime: "",
        presentTime: "",
      },
      status: this.$route.query.status,
      selectedProjects: [],
    };
  },
  computed: {
    paginatedData() {
      const start = (this.currentPage - 1) * this.pageSize;
      return this.filteredData.slice(start, start + this.pageSize);
    },
  },
  methods: {
    formatStatus(status) {
      const statusMap = {
        0: "项目初始",
        1: "接受申报",
        2: "已结束",
        "-1": "已禁用",
      };
      return statusMap[status] || "未知状态";
    },
    formatDate(dateString) {
      if (!dateString) return "无";
      const date = new Date(dateString);
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, "0"); // Months are zero-based
      const day = String(date.getDate()).padStart(2, "0");
      const hours = String(date.getHours()).padStart(2, "0");
      const minutes = String(date.getMinutes()).padStart(2, "0");
      const seconds = String(date.getSeconds()).padStart(2, "0");

      return `${year}年${month}月${day}日 ${hours}时${minutes}分${seconds}秒`;
    },
    handlePageChange(page) {
      this.currentPage = page;
    },
    filterData() {
      this.filteredData = this.tableData.filter((item) => {
        const matchesName = item.projectName.includes(this.searchProjectName);
        const matchesType = this.searchProjectType
          ? item.projectType === this.searchProjectType
          : true;
        return matchesName && matchesType;
      });
      this.currentPage = 1; // Reset to the first page after filtering
    },
    changeStatus(row, newStatus) {
      this.form = {
        projectName: row.projectName,
        projectStatus: newStatus,
        projectType: row.projectType,
        projectYear: row.projectYear,
      };
      axios
        .post("/api/project/update", this.form)
        .then(() => {
          this.$message.success("状态修改成功");
          this.fetchData();
        })
        .catch(() => {
          this.$message.error("状态修改失败");
        });
    },
    async fetchData() {
      try {
        const response = await axios.get("/api/project", {
          params: { status: this.status },
        });
        this.tableData = response.data; // Assign fetched data to tableData
        this.filteredData = response.data; // Initialize filtered data
      } catch (error) {
        console.error("数据加载失败:", error);
      }
    },
    zhiding() {
      if (this.selectedProjects.length === 0) {
        this.$message.warning("请选择至少一个项目");
        return;
      }

      const payload = {
        ids: this.selectedProjects, // 将选择的项目 ID 放入数组中
      };

      axios
        .delete(`/api/project/delete`, { data: payload }) // 将 payload 作为请求体发送
        .then(() => {
          this.$message.success("批量删除成功");
          this.fetchData();
          this.selectedProjects = []; // 删除后清空选择
        })
        .catch(() => {
          this.$message.error("批量删除失败");
        });
    },
    xiugai(pid) {
      this.$router.push({ path: "/change", query: { projectId: pid } });
    },
    delete1(pid) {
      this.$confirm("此操作将永久删除该项目, 是否继续?")
        .then(() => {
          axios
            .delete(`/api/project/delete2/${pid}`)
            .then(() => {
              this.$message.success("删除成功");
              this.fetchData();
            })
            .catch(() => {
              this.$message.error("删除失败");
            });
        })
        .catch(() => {
          this.$message.info("已取消删除");
        });
    },
    handleSelect(selection) {
      this.selectedProjects = selection.map((item) => item.pid);
      console.log("Selected projects:", this.selectedProjects);
    },
    handleSelectAll(selection) {
      this.selectedProjects = selection.map((item) => item.pid);
      console.log("All selected projects:", this.selectedProjects);
    },
  },
  mounted() {
    this.fetchData(); // Fetch data when component mounts
  },
};
</script>

<style scoped>
.plain-link {
  text-decoration: none; /* Remove underline */
  color: inherit; /* Inherit color from parent */
  cursor: pointer; /* Change cursor to pointer */
}
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
.el-table {
  margin-bottom: 20px;
}
</style>