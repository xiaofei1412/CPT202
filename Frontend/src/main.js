import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import App from './App.vue'
import router from './router'
import pinia from './store'
import './styles/main.css'
// 导入VueUse Motion动画插件 - 用于添加元素动画效果
import { MotionPlugin } from '@vueuse/motion'

// Font Awesome 图标库配置 - 用于全局提供丰富的图标
import { library } from '@fortawesome/fontawesome-svg-core'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
// 导入需要使用的图标组件 - 按类别分组
import { 
  faBook, faUserSecret, faHeart, faHome, faTasks, 
  faCalendarAlt, faChartLine, faCog, faSignOutAlt,
  faEdit, faTrash, faEye, faPlus, faSave, faTimes,
  faSearch, faFilter, faSort, faSortUp, faSortDown,
  faSpinner, faCircleNotch, faBell, faHeading, 
  faListUl, faQuoteLeft, faTag
} from '@fortawesome/free-solid-svg-icons'
import { 
  faBookmark, faComment, faClock, faCalendar, 
  faThumbsUp, faThumbsDown, faFile, faUser
} from '@fortawesome/free-regular-svg-icons'
import { 
  faGithub, faTwitter, faFacebook, faWeixin, 
  faQq, faWeibo
} from '@fortawesome/free-brands-svg-icons'

// 将导入的图标添加到图标库中，使其全局可用
library.add(
  // 实心图标 - 用于主要操作和功能
  faBook, faUserSecret, faHeart, faHome, faTasks, 
  faCalendarAlt, faChartLine, faCog, faSignOutAlt,
  faEdit, faTrash, faEye, faPlus, faSave, faTimes,
  faSearch, faFilter, faSort, faSortUp, faSortDown,
  faSpinner, faCircleNotch, faBell, faHeading,
  faListUl, faQuoteLeft, faTag,
  // 线性图标 - 用于次要操作和状态指示
  faBookmark, faComment, faClock, faCalendar,
  faThumbsUp, faThumbsDown, faFile, faUser,
  // 品牌图标 - 用于社交媒体链接
  faGithub, faTwitter, faFacebook, faWeixin,
  faQq, faWeibo
)

// 创建Vue应用实例
const app = createApp(App)

// 注册FontAwesome图标组件 - 使其在模板中可以通过font-awesome-icon标签使用
app.component('font-awesome-icon', FontAwesomeIcon)

// 注册Element Plus的所有图标组件 - 使其在模板中可以直接使用
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

// 添加路由导航调试 - 在开发时帮助追踪路由变化
router.beforeEach((to, from, next) => {
  console.log(`[路由导航] 从 ${from.fullPath} 到 ${to.fullPath}`)
  next()
})

router.afterEach((to, from) => {
  console.log(`[路由导航完成] 当前路由: ${to.fullPath}`)
})

// 注册插件到应用
app.use(ElementPlus)  // 注册Element Plus UI库
app.use(router)       // 注册Vue Router路由插件
app.use(pinia)        // 注册Pinia状态管理插件
// 注册VueUse Motion动画插件 - 提供v-motion指令以实现复杂的动画效果
app.use(MotionPlugin)

// 将应用挂载到DOM元素
app.mount('#app')
