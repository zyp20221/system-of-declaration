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
              <h2>申报书的评审老师指定</h2>
            </el-col>
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
              <el-input
                placeholder="输入项目年份"
                v-model="searchProjectYear"
                style="width: 200px; margin-right: 10px"
              ></el-input>
              <el-button
                @click="filterData"
                type="primary"
                style="margin-left: 10px"
                >搜索</el-button
              >
              <el-button @click="zhiding()" style="margin-left: 420px"
                >确认评审老师</el-button
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
              prop="bid"
              label="项目编号"
              width="150"
            ></el-table-column>
            <el-table-column
              prop="projectName"
              label="项目名称"
              width="150"
            ></el-table-column>
            <el-table-column
              prop="projectType"
              label="项目类型"
              width="90"
            ></el-table-column>
            <el-table-column
              prop="declarationName"
              label="申报书名称"
              width="90"
            ></el-table-column>
            <el-table-column
              prop="username"
              label="第一主持人"
              width="90"
            ></el-table-column>
            <el-table-column
              prop="adviser"
              label="第一指导教师"
              width="90"
            ></el-table-column>
            <el-table-column
              prop="projectYear"
              label="所属批次"
              width="90"
            ></el-table-column>
            <el-table-column label="操作" width="460">
              <template v-slot="scope">
                <el-button @click="view(scope.row.bid)"> 查看</el-button>
                <el-button @click="look(scope.row.bid)">
                  查看指定的评审老师
                </el-button>
                <el-button @click="deleteItem(scope.row.bid)"> 删除 </el-button>
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
      ids: this.$route.query.ids, // 获取路由参数
      selectedProjects: [],
      searchProjectName: "",
      searchProjectType: "",
      searchProjectYear: "",
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
        const response = await axios.get("/api/declaration/all");
        this.tableData = response.data; // 将获取的数据赋值给 tableData
        this.filteredData = this.tableData; // 初始化 filteredData
      } catch (error) {
        console.error("获取数据失败:", error);
        this.$message.error("获取数据失败，请重试");
      }
    },
    handlePageChange(newPage) {
      this.currentPage = newPage;
    },
    view(bid) {
      this.$router.push({
        path: "/xiangqing",
        query: { bid: bid },
      });
    },
    async deleteItem(bid) {
      try {
        await axios.delete(`/api/declaration/delete/${bid}`);
        this.$message.success("删除成功");
        this.fetchData();
      } catch (error) {
        console.error("删除失败:", error);
        this.$message.error("删除失败，请重试");
      }
    },
    handleSelect(selection) {
      this.selectedProjects = selection.map((item) => item.bid);
    },
    handleSelectAll(selection) {
      this.selectedProjects = selection.map((item) => item.bid);
    },
    zhiding() {
      if (this.selectedProjects.length === 0) {
        this.$message.warning("请选择至少一个项目");
        return;
      }

      const payload = {
        ids: this.ids ? this.ids.split(",") : [], // 确保 ids 是一个数组
        existingIds: this.selectedProjects, // 选中的项目
      };

      axios
        .post("/api/declaration/zhiding", payload)
        .then(() => {
          this.$message.success("评审老师指定成功");
          this.fetchData(); // 刷新数据
        })
        .catch((error) => {
          console.error("指定评审老师失败:", error);
          this.$message.error("指定评审老师失败，请重试");
        });
    },
    filterData() {
      this.filteredData = this.tableData.filter((item) => {
        const matchesName = item.projectName.includes(this.searchProjectName);
        const matchesType = this.searchProjectType
          ? item.projectType === this.searchProjectType
          : true;
        const matchesYear = this.searchProjectYear
          ? item.projectYear === this.searchProjectYear
          : true;
        return matchesName && matchesType && matchesYear;
      });
      this.currentPage = 1; // Reset to the first page after filtering
    },
    look(bid) {
      // 将当前行的 bid 添加到 selectedProjects 数组
      if (!this.selectedProjects.includes(bid)) {
        this.selectedProjects.push(bid);
      }
      // 跳转到指定路由并传递 bid 数组
      this.$router.push({
        path: "/zhiding",
        query: {
          ids: this.selectedProjects.join(","),
        },
      });
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