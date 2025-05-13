import { request } from './request'

// 获取验证码
export function getCaptcha(email) {
  return request({
    url: '/cpt/captcha/generate',
    method: 'GET',
    params: {
      email: email
    }
  })
}

// cpt/register
export function register(data) {
  return request({
    url: '/cpt/register',
    method: 'POST',
    data: data
  })
}
// cpt/login
export function login(data) {
  return request({
    url: '/cpt/login',
    method: 'POST',
    data: data
  })
}

// /cpt/users/update 
export function updateUser(data) {
  return request({
    url: '/cpt/users/update',
    method: 'POST',
    data: data
  })
}

// /cpt/users/{id}
export function getUser(id) {
  return request({
    url: `/cpt/users/${id}`,
    method: 'GET'
  })
}

// /cpt/users/delete/{id}
export function deleteUser(id) {
  return request({
    url: `/cpt/users/delete/${id}`,
    method: 'GET'
  })
}

// /cpt/users/deleteByAccountAndPassword GET
export function deleteByAccountAndPassword(account, password) {
  return request({
    url: `/cpt/users/deleteByAccountAndPassword?account=${account}&password=${password}`,
    method: 'GET'
  })
}

// GET http://localhost:8888/cpt/users/list
export function getUserList(params) {
  return request({
    url: '/cpt/users/list',
    method: 'GET',
    params: params
  })
}
// /cpt/users/updatepwd  
export function updatePwd(data) {
  return request({
    url: '/cpt/users/updatepwd',
    method: 'get',
    params: data
  })
}

// /cpt/appeal
export function appeal(data) {
  return request({
    url: '/cpt/appeals/create',
    method: 'POST',
    data: data
  })  
}
