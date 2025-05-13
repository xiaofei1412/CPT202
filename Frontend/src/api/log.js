import { request } from './request'


// http://localhost:8888/cpt/logs/admin/list POST
export const getLogList = (data)=>{
  return request({
    url:'/cpt/logs/admin/list',
    method:'post',
    data
  })
}

// http://localhost:8888/cpt/logs/update POST
export const updateLog = (data)=>{
  return request({
    url:'/cpt/logs/update',
    method:'post',
    data
  })
}

// http://localhost:8888/cpt/logs/delete/{id}
export const deleteLogs = (id)=>{
  return request({
    url:`/cpt/logs/delete/${id}`,
    method:'GET'
  })
}
