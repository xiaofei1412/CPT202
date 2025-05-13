<script setup>
// 此组件是根组件，作为整个应用的入口点
// 不需要导入其他组件，因为它只用于提供路由出口
</script>

<template>
  <!-- 路由视图容器 - 使用<router-view>作为路由组件的显示位置 -->
  <router-view v-slot="{ Component }">
    <!-- 页面切换动画 - 使用过渡效果实现平滑的页面切换 -->
    <transition name="fade" mode="out-in">
      <!-- 动态渲染当前路由匹配的组件 -->
      <component :is="Component" />
    </transition>
  </router-view>
</template>

<style scoped>
/* Logo相关样式 - 可能用于首页或加载页 */
.logo {
  height: 6em;
  padding: 1.5em;
  will-change: filter; /* 优化动画性能的提示 */
  transition: filter 300ms; /* 添加过滤器效果的平滑过渡 */
}
.logo:hover {
  filter: drop-shadow(0 0 2em #646cffaa); /* 鼠标悬停时添加阴影效果 */
}
.logo.vue:hover {
  filter: drop-shadow(0 0 2em #42b883aa); /* Vue logo特定的悬停效果，使用Vue品牌色 */
}
</style>
