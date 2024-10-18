// src/axios-config.js

import axios from 'axios';
import Vue from 'vue';

const setupAxiosInterceptors = () => {
  // Request interceptor
  axios.interceptors.request.use(
    (config) => {
      const token = localStorage.getItem("token");
      if (token) {
        config.headers.Authorization = `Bearer ${token}`;
      }
      return config;
    },
    (error) => {
      return Promise.reject(error);
    }
  );

  // Response interceptor for handling 401 errors
  axios.interceptors.response.use(
    (response) => response,
    (error) => {
      if (error.response && error.response.status === 401) {
        Vue.prototype.$message.error("未授权，请登录后再试");
        // Optionally redirect to login page
        // this.$router.push('/login');
      }
      return Promise.reject(error);
    }
  );
};

export default setupAxiosInterceptors;