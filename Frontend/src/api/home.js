import { request } from './request'

// cpt/logs/platform/data
export const getPlatformData = () => {
  return request({
    url: '/cpt/logs/platform/data',
    method: 'GET'
  })
}
// 仪表盘
export const getDashboardData = (params) => {
  return request({
    url: '/cpt/logs/my/data',
    method: 'GET',
    params: params
  })
}


// cpt/logs/create
export const createLog = (data) => {
  return request({
    url: '/cpt/logs/create',
    method: 'POST',
    data: data
  })
}
// cpt/logs/list
export const getLogList = (params) => {
  return request({
    url: '/cpt/logs/list',
    method: 'POST',
    data: params
  })
}

// cpt/logs/1
export const getLogDetail = (id) => {
  return request({
    url: `/cpt/logs/${id}`,
    method: 'GET'
  })
}
// 删除
export const deleteLog = (id) => {
  return request({
    url: `/cpt/logs/delete/${id}`,
    method: 'get'
  })
}
// 更新
export const updateLog = ( data) => {
  return request({
    url: `/cpt/logs/update`,
    method: 'post',
    data: data
  })
}