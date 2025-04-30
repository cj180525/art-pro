import { createApp } from 'vue'
import App from './App.vue'
import router from "@/router";
import axios from "axios";

// axios.defaults.baseURL = 'http://localhost:8080'
axios.defaults.baseURL = 'http://123.60.104.131:3081'

const app = createApp(App)

app.use(router)

app.mount('#app')
