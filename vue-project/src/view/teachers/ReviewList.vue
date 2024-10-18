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
                to="/teachers"
                style="font-size: large; font-weight: bold"
                >主页</router-link
              >
            </el-menu-item>
            <el-submenu index="2">
              <template #title>申报管理</template>
              <el-menu-item index="2-1">
                <router-link to="/teacherdeclare">申报项目</router-link>
              </el-menu-item>
            </el-submenu>
            <el-menu-item index="3">
              <router-link to="/reviewList">指导项目</router-link>
            </el-menu-item>
            <el-menu-item index="4">
              <router-link to="/pingshenlist">评审项目</router-link>
            </el-menu-item>
            <el-menu-item index="5">
              <router-link to="/teacherspersonal">个人中心</router-link>
            </el-menu-item>
          </el-menu>
        </el-aside>
        <el-main>
          <h2>指导项目管理</h2>
          <el-row>
            <el-col :span="4">
              <el-input placeholder="项目编号" v-model="projectCode"></el-input>
            </el-col>
            <el-col :span="4">
              <el-input placeholder="项目名称" v-model="projectName"></el-input>
            </el-col>
            <el-col :span="4">
              <el-button type="primary" @click="search">查询</el-button>
              <el-button @click="reset">重置</el-button>
            </el-col>
          </el-row>
          <el-table
            :data="paginatedData"
            style="width: 100%"
            :row-class-name="getRowClassName"
          >
          <el-table-column
              prop="projectId"
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
              width="150"
            ></el-table-column>
            <!-- <el-table-column
              prop="projectLevel"
              label="项目级别"
              width="90"
            ></el-table-column> -->
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
            <el-table-column label="操作" width="400">
              <template v-slot="scope">
                <el-button 
                  :disabled="scope.row.pingShen !== 1"  
                  @click="review(scope.row.bid)">查看详情</el-button>
              </template>
            </el-table-column>
          </el-table>

          <el-pagination
            background
            layout="total, prev, pager, next"
            :total="tableData.length"
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
import axios from 'axios';

export default {
  data() {
    return {
      selectedBatch: "",
      projectName: "",
      projectCode: "",
      selectedType: "",
      tableData: [],
      currentPage: 1,
      pageSize: 10,
      notifications: [],
      form10:{
        lastNotificationCount: 0,
      }
    };
  },
  computed: {
    paginatedData() {
      const start = (this.currentPage - 1) * this.pageSize;
      return this.tableData.slice(start, start + this.pageSize);
    },
    showNotificationDot() {
      // 只有当通知数量大于上次通知数量时才显示红点
      return this.notifications.length > this.form10.lastNotificationCount; // 只有当数量增加时显示红点
    },
  },
  methods: {
    async fetchData() {
      try {
        const response = await axios.get("api/review/list");
        this.tableData = response.data; // Assuming response.data is the List<DeclareVo>
      } catch (error) {
        console.error("Error fetching data:", error);
      }
    },
    async search() {
      const params = {};
      if (this.selectedBatch) params.batch = this.selectedBatch;
      if (this.projectCode) params.projectCode = this.projectCode;
      if (this.projectName) params.projectName = this.projectName;
      if (this.selectedType) params.projectType = this.selectedType;

      try {
        const response = await axios.get("api/declaration/search", { params });
        this.tableData = response.data; // Update table data with the search results
      } catch (error) {
        console.error("Error searching:", error);
      }
    },
    reset() {
      this.selectedBatch = "";
      this.projectCode = "";
      this.projectName = "";
      this.selectedType = "";
      this.fetchData();
    },
    review(bid) {
      this.$router.push({ path: '/review', query: { bid } });
    },
    showNotifications() {
      this.form10.lastNotificationCount = this.notifications.length; // 更新上次数量
      axios.post('/api/project/update2',this.form10); // 发送请求传数据
      this.$router.push("/teachersnotice");
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
  mounted() {
    this.fetchData();
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
.green-background {
  background-color: green;
  color: white; /* 可选，调整文字颜色 */
}
</style>