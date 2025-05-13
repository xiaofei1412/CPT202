import { request } from './request'

// /cpt/messages/list
export const getMessageList =()=>{
  const userId = JSON.parse(localStorage.getItem('user')).userId
  return request({
    url:'/cpt/messages/list',
      method: 'GET',
        params:{
        pageNum:1,
            pageSize: 1000,
            userId:userId
        }
  })
}

// 标记已读
export const markRead = (data)=>{
  return request({
    url:'/cpt/messages/update',
    method:'POST',
    data
  })
}
// 删除消息
export const deleteMessage = (id)=>{
  return request({
    url:'/cpt/messages/delete/'+id,
    method:'get',
  })
}
