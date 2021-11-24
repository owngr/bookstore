import { createApp } from 'vue'
import App from './App.vue'
import router from './router' // <---
import PrimeVue from 'primevue/config';
import DataTable from "primevue/datatable";
import Column from "primevue/column";
import Textarea from "primevue/textarea";
import Message from 'primevue/message';
import Menubar from 'primevue/menubar';
import InputNumber from 'primevue/inputnumber';
import InputText from 'primevue/inputtext';
import InputMask from 'primevue/inputmask';
import Dropdown from 'primevue/dropdown';
import Button from 'primevue/button';
import Dialog from "primevue/dialog";
import AutoComplete from "primevue/autocomplete";
import FileUpload from 'primevue/fileupload';
// import 'primevue/resources/themes/bootstrap4-light-blue/theme.css';

import "primevue/resources/themes/saga-blue/theme.css";
import 'primeicons/primeicons.css';
import 'primeflex/primeflex.css';
const app = createApp(App)

app.use(PrimeVue)
app.use(router)
app.component('DataTable', DataTable)
app.component('Column', Column)
app.component('Textarea', Textarea)
app.component('Message', Message)
app.component('Menubar', Menubar)
app.component('Button', Button)
app.component('InputNumber', InputNumber)
app.component('InputText', InputText)
app.component('InputMask', InputMask)
app.component('Dropdown', Dropdown)
app.component('Dialog', Dialog)
app.component('AutoComplete', AutoComplete)
app.component('FileUpload', FileUpload)
app.mount('#app')
