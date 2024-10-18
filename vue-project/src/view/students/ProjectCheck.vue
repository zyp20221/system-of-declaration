<template>
  <div id="app">
    <el-container style="overflow: hidden">
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
        <el-main style="overflow-y: auto">
          <h2>项目查看</h2>
          <el-table :data="paginatedData" style="width: 100%">
            <el-table-column
              prop="projectName"
              label="项目名称"
              width="180"
            ></el-table-column>
            <el-table-column
              prop="projectType"
              label="项目类型"
              width="180"
            ></el-table-column>
            <el-table-column prop="publishTime" label="发布时间" width="100">
              <template #default="{ row }">
                <span>{{ formatDate(row.publishTime) }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="endTime" label="结束时间" width="100">
              <template #default="{ row }">
                <span>{{ formatDate(row.endTime) }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="projectStatus" label="项目状态" width="150">
              <template #default="{ row }">
                <span>{{ formatStatus(row.projectStatus) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="480">
              <template #default="{ row }">
                <el-button
                  @click="shenbao(row.pid)"
                  type="primary"
                  :disabled="row.projectStatus !== 1"
                  > 点击申报
                </el-button>
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
import axios from "axios";

export default {
  data() {
    return {
      tabPosition: "left",
      activeMenu: "",
      tableData: [],
      currentPage: 1,
      pageSize: 10,
      form: {
        projectName: "",
        projectStatus: "",
        projectType: "",
        projectYear: "",
        publishTime : "",
        endTime : "",
      },
      notifications: [],
      form10: {
        lastNotificationCount: 0,
      },
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
    handlePageChange(page) {
      this.currentPage = page;
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
    async fetchData() {
      try {
        const response = await axios.get("/api/project/length");
        this.tableData = response.data; // 将请求的数据赋给 tableData
      } catch (error) {
        console.error("数据加载失败:", error);
      }
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
    formatStatus(status) {
      switch (status) {
        case 0:
          return "项目初始";
        case 1:
          return "接受申报";
        case 2:
          return "已结束";
        case -1:
          return "已禁用";
        default:
          return "未知状态";
      }
    },
    shenbao(pid) {
      this.$router.push({ path: "/shenbao", query: { id: pid } });
    },
  },
  mounted() {
    this.fetchData(); // 在组件挂载时获取数据
    this.fetchNotifications(); // 组件挂载时获取通知
  },
};
</script>
  
  <style scoped>
.plain-link {
  text-decoration: none; /* 去掉下划线 */
  color: inherit; /* 继承父元素的颜色 */
  cursor: pointer; /* 使光标变为指针 */
}
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
.el-table {
  margin-bottom: 20px;
}
</style>