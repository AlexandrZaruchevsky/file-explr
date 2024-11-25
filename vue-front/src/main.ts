import './index.css'

import { createApp } from 'vue'
import App from './App.vue'
import components from './components'

const app = createApp(App)
components.forEach(c => app.component(c.name as string, c))

app.mount('#app')
