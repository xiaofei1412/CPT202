import { defineStore } from 'pinia'

export const useLogStore = defineStore('log', {
  state: () => ({
    logs: [], // All logs
    currentLog: null, // Currently viewed log
    loading: false,
    totalLogs: 0,
    readingTime: 0, // Total reading time
  }),
  
  getters: {
    recentLogs: (state) => {
      return [...state.logs].sort((a, b) => new Date(b.date) - new Date(a.date)).slice(0, 3)
    },
  },
  
  actions: {
    // Mock fetch logs list
    fetchLogs(params = {}) {
      this.loading = true
      
      // Mock API call
      return new Promise((resolve) => {
        setTimeout(() => {
          // Generate mock data
          const bookTitles = [
            "To Kill a Mockingbird",
            "1984",
            "Pride and Prejudice",
            "The Great Gatsby",
            "The Catcher in the Rye",
            "Jane Eyre",
            "Animal Farm",
            "Lord of the Flies",
            "The Hobbit",
            "Brave New World"
          ];
          
          const authors = [
            "Harper Lee",
            "George Orwell",
            "Jane Austen",
            "F. Scott Fitzgerald",
            "J.D. Salinger",
            "Charlotte Brontë",
            "J.R.R. Tolkien",
            "William Golding",
            "Aldous Huxley",
            "Ernest Hemingway"
          ];
          
          const mockLogs = Array.from({ length: 10 }, (_, i) => ({
            id: i + 1,
            title: bookTitles[i],
            author: authors[i % authors.length],
            date: new Date(Date.now() - i * 86400000).toISOString().split('T')[0], // Date, one per day
            duration: Math.floor(Math.random() * 120) + 30, // 30-150 minutes randomly
            content: `These are my reading notes for ${bookTitles[i]}. The book explores themes of ${i % 2 === 0 ? 'human nature and society' : 'personal growth and relationships'}, with compelling characters and a well-crafted narrative structure.`,
            status: ['approved', 'pending', 'rejected'][Math.floor(Math.random() * 3)] // Random status
          }))
          
          this.logs = mockLogs
          this.totalLogs = 100 // Assume 100 total logs
          this.readingTime = mockLogs.reduce((sum, log) => sum + log.duration, 0)
          this.loading = false
          
          resolve({
            logs: mockLogs,
            total: 100
          })
        }, 500)
      })
    },
    
    // Get log details
    fetchLogDetail(id) {
      this.loading = true
      
      // Mock API call
      return new Promise((resolve) => {
        setTimeout(() => {
          const log = this.logs.find(l => l.id == id) || {
            id: id,
            title: `Classic Literature Analysis ${id}`,
            author: "John Smith",
            date: new Date().toISOString().split('T')[0],
            duration: 60,
            content: 'This detailed reading journal captures my understanding and reflections on this classic work.\n\nIt includes analysis of the main plot, character interpretations, and my personal reading experience.\n\nThe book primarily discusses themes of morality, society, and personal growth...',
            status: 'approved'
          }
          
          this.currentLog = log
          this.loading = false
          
          resolve(log)
        }, 300)
      })
    },
    
    // Create log
    createLog(logData) {
      this.loading = true
      
      // Mock API call
      return new Promise((resolve) => {
        setTimeout(() => {
          const newLog = {
            id: this.logs.length + 1,
            ...logData,
            status: 'pending' // New logs are pending review by default
          }
          
          this.logs.unshift(newLog)
          this.loading = false
          
          resolve(newLog)
        }, 300)
      })
    },
    
    // Update log
    updateLog(id, logData) {
      this.loading = true
      
      // Mock API call
      return new Promise((resolve) => {
        setTimeout(() => {
          const index = this.logs.findIndex(l => l.id == id)
          
          if (index !== -1) {
            const updatedLog = { ...this.logs[index], ...logData }
            this.logs[index] = updatedLog
            
            if (this.currentLog && this.currentLog.id == id) {
              this.currentLog = updatedLog
            }
          }
          
          this.loading = false
          resolve(this.logs[index])
        }, 300)
      })
    },
    
    // Delete log
    deleteLog(id) {
      this.loading = true
      
      // Mock API call
      return new Promise((resolve) => {
        setTimeout(() => {
          this.logs = this.logs.filter(log => log.id != id)
          this.loading = false
          
          if (this.currentLog && this.currentLog.id == id) {
            this.currentLog = null
          }
          
          resolve({ success: true })
        }, 300)
      })
    }
  }
}) 