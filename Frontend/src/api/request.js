import axios from 'axios';

// export const baseURL = 'http://192.168.0.104:8888';
export const baseURL = 'http://116.62.42.63:8888';

export function request(config) {
  const instance = axios.create({
    baseURL,
    timeout: 50000 * 60,
  });

  // 请求拦截
  instance.interceptors.request.use(
    (config) => {
      let token = localStorage.getItem('token');
      if (token) {
        config.headers['Authorization'] = token;
      }
      return config;
    },
    (err) => {
      console.log(err);
    }
  );

  //响应拦截
  instance.interceptors.response.use(
    (res) => {
      return res.data;
    },
    (err) => {
      console.log(err);
    }
  );

  return instance(config);
}
