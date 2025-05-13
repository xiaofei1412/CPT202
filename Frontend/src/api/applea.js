import { request } from './request'


// GET http://localhost:8888/cpt/appeals/list
export const getAppealsList = (data)=>{
  let url = '/cpt/appeals/list?'
  Object.keys(data).forEach(key => {
    url += `${key}=${data[key]}&`
  })
  url = url.slice(0, -1)

  return request({
    url,
    method:'get',
  })
}

// http://localhost:8888/cpt/appeals/update
export const updateAppeal = (data)=>{
  return request({
    url:'/cpt/appeals/update',
    method:'post',
    data
  })
}