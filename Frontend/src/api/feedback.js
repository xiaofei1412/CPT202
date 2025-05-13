import { request } from './request'


// http://localhost:8888/cpt/feedback/create 
export const createFeedback = (data)=>{
  return request({
    url:'/cpt/feedback/create',
    method:'post',
    data
  })
}

// http://localhost:8888/cpt/feedback/list
export const getFeedbackList = (data)=>{
  // 拼接在路径
  let url = '/cpt/feedback/list?'
  Object.keys(data).forEach(key => {
    url += `${key}=${data[key]}&`
  })
  url = url.slice(0, -1)

  return request({
    url,
    method:'get',
  })
}

// /cpt/feedback/update
export const updateFeedback = (data)=>{
  return request({
    url:'/cpt/feedback/update',
    method:'post',
    data
  })
}