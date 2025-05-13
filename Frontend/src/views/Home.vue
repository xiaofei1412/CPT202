<template>
  <MainLayout>
    <!-- 顶部横幅区域/首屏展示区 -->
    <section class="hero">
      <!-- 左侧内容区 - 带有动画效果 -->
      <div class="hero-content" ref="heroContent" v-motion>
        <h1 class="hero-title">Record Reading, Share Growth</h1>
        <p class="hero-subtitle">Professional Reading Activity Management System, Make Your Reading More Valuable</p>
        <!-- 操作按钮区 - 带有悬停放大效果 -->
        <div class="hero-actions">
          <el-button type="primary" size="large" @click="handleStartRecording" 
            v-motion="{ hover: { scale: 1.1 } }">Start Recording</el-button>
        </div>
      </div>
      <!-- 右侧图片区 - 带有动画效果 -->
      <div class="hero-image" ref="heroImage" v-motion>
        <img src="../assets/hero-image.svg" alt="Reading Management" />
      </div>
    </section>
    
    <!-- 核心功能特点展示区 -->
    <section class="features">
      <!-- 标题 - 带有向上淡入动画 -->
      <h2 class="section-title" data-aos="fade-up">Core Features</h2>
      
      <!-- 功能卡片网格布局 -->
      <div class="feature-grid">
        <!-- 功能卡片 - 循环渲染，带有延迟淡入动画 -->
        <div class="feature-card" 
          v-for="(feature, index) in features" 
          :key="index"
          data-aos="fade-up"
          :data-aos-delay="index * 100">
          <!-- 功能图标 (增大尺寸) -->
          <el-icon :size="42" class="feature-icon">
            <component :is="feature.icon"></component>
          </el-icon>
          <!-- 功能标题和描述 -->
          <h3 class="feature-title">{{ feature.title }}</h3>
          <p class="feature-desc">{{ feature.description }}</p>
        </div>
      </div>
    </section>
    
    <!-- 平台数据统计展示区 -->
    <section class="stats" data-aos="fade-up">
      <!-- 标题 -->
      <h2 class="section-title">Platform Data</h2>
      
      <!-- 数据卡片网格布局 -->
      <div class="stats-grid">
        <!-- 数据卡片 - 循环渲染，带有缩放动画和延迟效果 -->
        <div class="stat-card" 
          v-for="(stat, index) in stats" 
          :key="index"
          data-aos="zoom-in"
          :data-aos-delay="index * 100">
          <!-- 数据值和标签 -->
          <div class="stat-value">{{ stat.value }}</div>
          <div class="stat-label">{{ stat.label }}</div>
        </div>
      </div>
    </section>
    
    <!-- 号召用户行动区 -->
    <section class="cta" data-aos="fade-up">
      <h2 class="cta-title">Start Your Reading Journey</h2>
      <p class="cta-desc">Join Us, Record Every Reading, Share Every Step of Growth</p>
      <!-- 注册按钮 - 带有悬停放大效果 -->
      <el-button type="primary" size="large" 
        @click="$router.push('/auth')"
        v-motion="{ hover: { scale: 1.1 } }">Free Registration</el-button>
    </section>
  </MainLayout>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import MainLayout from '../layouts/MainLayout.vue'
import { Document, Reading, DataAnalysis, Timer, User, Share } from '@element-plus/icons-vue'
import { useMotion } from '@vueuse/motion'
import gsap from 'gsap'
import AOS from 'aos'
import 'aos/dist/aos.css'
import { getPlatformData } from '../api/home'
import { useRouter } from 'vue-router'

const router = useRouter()
const handleStartRecording = () => {
  const user = JSON.parse(localStorage.getItem('user'))
  if (user) {
    router.push('/logs')
  } else {
    router.push('/auth')
  }
}

// 初始化 AOS 动画库
onMounted(() => {
  AOS.init({
    duration: 1000, // 动画持续时间（毫秒）
    once: true      // 动画只播放一次
  })
})

// Hero 区域动画引用
const heroContent = ref(null) // 内容区域引用
const heroImage = ref(null)   // 图片区域引用

// VueUse Motion 动画配置 - 内容区域从左侧滑入
const { motion: heroMotion } = useMotion(heroContent, {
  initial: { opacity: 0, x: -50 },  // 初始状态：透明且左移50px
  enter: { opacity: 1, x: 0, transition: { duration: 800 } } // 进入状态：完全不透明且回到原位
})

// 图片区域从右侧滑入
const { motion: imageMotion } = useMotion(heroImage, {
  initial: { opacity: 0, x: 50 },   // 初始状态：透明且右移50px
  enter: { opacity: 1, x: 0, transition: { duration: 800 } } // 进入状态：完全不透明且回到原位
})

// GSAP 动画 - 数据值从0递增到目标值
onMounted(() => {
  gsap.from('.stat-value', {
    textContent: 0,       // 从0开始
    duration: 2,          // 动画持续2秒
    ease: 'power1.out',   // 缓动函数
    snap: { textContent: 1 }, // 确保数值为整数
    stagger: 0.2          // 每个数据卡片延迟0.2秒开始动画
  })
})

// 功能特点数据配置 (移除分享和推荐)
const features = ref([
  {
    icon: 'Document', // 文档图标
    title: 'Reading Log', // 阅读日志功能
    description: 'Record Your Reading Notes, Thoughts, and Experiences Forever' // 功能描述
  },
  {
    icon: 'Timer', // 时钟图标
    title: 'Time Statistics', // 时间统计功能
    description: 'Automatically Track Reading Time and Quantify Your Reading Habits' // 功能描述
  },
  {
    icon: 'DataAnalysis', // 数据分析图标
    title: 'Data Analysis', // 数据分析功能
    description: 'Visualize Reading Data to Help You Understand Your Reading Patterns' // 功能描述
  },
  {
    icon: 'Reading', // 阅读图标
    title: 'Reading Planning', // 阅读计划功能
    description: 'Create Reading Plans and Track Reading Progress' // 功能描述
  }
])

// 平台数据统计配置
const stats = ref([
])
getPlatformData().then(res => {
    console.log(res.data)
  
  
    stats.value.push({
      value: res.data.sumUser,
      label: 'Registered Users'
    })
    stats.value.push({
      value: res.data.sumReadingLog,
      label: 'Reading Logs'
    })
    stats.value.push({
      value: res.data.sumTime,
      label: 'Reading Hours'
    })
  })

</script>

<style scoped>
/* 顶部横幅/首屏区域样式 */
.hero {
  display: flex;
  align-items: center;
  background: linear-gradient(135deg, #f5f7fa 0%, #e4ecfb 100%); /* 渐变背景 */
  border-radius: 12px;
  padding: 60px 40px;
  margin-bottom: 60px;
  position: relative;
  overflow: hidden;
}

/* 内容区域样式 */
.hero-content {
  flex: 1;
  z-index: 2; /* 确保内容在图片上层 */
  max-width: 550px;
}

/* 主标题样式 */
.hero-title {
  font-size: 42px;
  font-weight: 700;
  color: var(--text-primary);
  margin-bottom: 16px;
}

/* 副标题样式 */
.hero-subtitle {
  font-size: 18px;
  color: var(--text-secondary);
  margin-bottom: 32px;
  max-width: 500px;
}

/* 操作按钮区域样式 */
.hero-actions {
  display: flex;
  gap: 16px;
}

/* 图片区域样式 */
.hero-image {
  flex: 1.2;
  display: flex;
  justify-content: center;
  align-items: center;
  overflow: hidden;
  z-index: 1;
  position: relative;
}

/* 图片样式及悬停效果 */
.hero-image img {
  max-width: 100%;
  height: auto;
  object-fit: contain;
  transition: transform 0.5s ease;
}

.hero-image img:hover {
  transform: scale(1.03); /* 悬停时图片轻微放大 */
}

/* 区域标题通用样式 */
.section-title {
  text-align: center;
  font-size: 32px;
  font-weight: 600;
  margin-bottom: 40px;
  color: var(--text-primary);
}

/* 功能展示网格布局 (修改为 2x2) */
.feature-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr); /* 两列布局 */
  gap: 40px; /* 增大间距 */
  margin-bottom: 60px;
  max-width: 1000px; /* 再次增大最大宽度 */
  margin-left: auto;
  margin-right: auto;
}

/* 功能卡片样式 (增大内边距) */
.feature-card {
  background-color: white;
  padding: 40px; /* 增大内边距 */
  border-radius: 8px;
  box-shadow: var(--card-shadow);
  text-align: center;
  transition: all 0.3s ease;
}

/* 功能卡片悬停效果 */
.feature-card:hover {
  transform: translateY(-5px); /* 向上移动 */
  box-shadow: 0 8px 24px rgba(0,0,0,0.1); /* 增强阴影效果 */
}

/* 功能图标样式 */
.feature-icon {
  color: var(--primary-color);
  margin-bottom: 20px; /* 稍微增大图标和标题间距 */
  transition: transform 0.3s ease;
}

/* 功能图标悬停效果 */
.feature-card:hover .feature-icon {
  transform: scale(1.2); /* 图标放大 */
}

/* 功能标题样式 */
.feature-title {
  font-size: 22px; /* 增大标题字体 */
  font-weight: 600;
  margin-bottom: 14px; /* 调整间距 */
  color: var(--text-primary);
}

/* 功能描述样式 */
.feature-desc {
  font-size: 15px; /* 可选：略微增大描述字体 */
  color: var(--text-secondary);
  line-height: 1.6;
}

/* 数据统计区域样式 */
.stats {
  margin-bottom: 60px;
}

/* 数据统计网格布局 */
.stats-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr); /* 三列布局 */
  gap: 20px;
  max-width: 900px;
  margin: 0 auto;
}

/* 数据卡片样式 */
.stat-card {
  background-color: white;
  padding: 24px;
  border-radius: 8px;
  box-shadow: var(--card-shadow);
  text-align: center;
  transition: all 0.3s ease;
}

/* 数据卡片悬停效果 */
.stat-card:hover {
  transform: translateY(-5px); /* 向上移动 */
  box-shadow: 0 8px 24px rgba(0,0,0,0.1); /* 增强阴影效果 */
}

/* 数据值样式 */
.stat-value {
  font-size: 36px;
  font-weight: 700;
  color: var(--primary-color);
  margin-bottom: 8px;
}

/* 数据标签样式 */
.stat-label {
  color: var(--text-secondary);
  font-size: 16px;
}

/* 号召用户行动区样式 */
.cta {
  background-color: #4A90E2;
  color: white;
  text-align: center;
  padding: 60px;
  border-radius: 12px;
  margin-bottom: 60px;
  transition: all 0.3s ease;
}

/* 号召用户行动区悬停效果 */
.cta:hover {
  transform: scale(1.02); /* 轻微放大 */
  box-shadow: 0 12px 32px rgba(74,144,226,0.3); /* 增强阴影效果 */
}

/* 号召行动区标题样式 */
.cta-title {
  font-size: 32px;
  font-weight: 600;
  margin-bottom: 16px;
}

/* 号召行动区描述样式 */
.cta-desc {
  font-size: 18px;
  margin-bottom: 32px;
  opacity: 0.9;
}

/* 响应式布局调整 */
@media (max-width: 992px) {
  .feature-grid {
    grid-template-columns: repeat(2, 1fr); /* 平板设备：两列布局 */
  }
  
  .stats-grid {
    grid-template-columns: repeat(2, 1fr); /* 平板设备：两列布局 */
  }
  
  .hero {
    padding: 40px 30px; /* 减小内边距 */
  }
}

@media (max-width: 768px) {
  .hero {
    flex-direction: column; /* 移动设备：垂直方向布局 */
    padding: 40px 20px;
    text-align: center;
  }
  
  .hero-content {
    max-width: 100%;
  }
  
  .hero-title {
    font-size: 32px; /* 移动设备：减小标题字体 */
  }
  
  .hero-actions {
    justify-content: center; /* 按钮居中 */
  }
  
  .hero-image {
    margin-top: 30px;
    width: 100%;
  }
  
  .feature-grid {
    grid-template-columns: 1fr; /* 移动设备：单列布局 */
  }
  
  .cta {
    padding: 40px 20px; /* 减小内边距 */
  }
}
</style> 