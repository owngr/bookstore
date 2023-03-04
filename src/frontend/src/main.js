import { createApp } from 'vue'
import App from './App.vue'
import router from './router' // <---
import PrimeVue from 'primevue/config'
import DataTable from "primevue/datatable"
import Column from "primevue/column"
import ColumnGroup from "primevue/columngroup"
import Row from "primevue/row"
import Textarea from "primevue/textarea"
import Message from 'primevue/message'
import Menubar from 'primevue/menubar'
import InputNumber from 'primevue/inputnumber'
import InputText from 'primevue/inputtext'
import InputMask from 'primevue/inputmask'
import Dropdown from 'primevue/dropdown'
import Button from 'primevue/button'
import Dialog from "primevue/dialog"
import AutoComplete from "primevue/autocomplete"
import FileUpload from 'primevue/fileupload'
import DataView from 'primevue/dataview'
import SelectButton from 'primevue/selectbutton';
// import 'primevue/resources/themes/bootstrap4-light-blue/theme.css';


import VueDatePicker from '@vuepic/vue-datepicker';
import '@vuepic/vue-datepicker/dist/main.css';

import "primevue/resources/themes/saga-blue/theme.css"
import 'primeicons/primeicons.css'
import 'primeflex/primeflex.css'
const app = createApp(App)

app.use(PrimeVue)
app.use(router)
app.component('DataTable', DataTable)
app.component('PColumn', Column)
app.component('ColumnGroup', ColumnGroup)
app.component('PRow', Row)
app.component('TextArea', Textarea)
app.component('PMessage', Message)
app.component('MenuBar', Menubar)
app.component('PButton', Button)
app.component('InputNumber', InputNumber)
app.component('InputText', InputText)
app.component('InputMask', InputMask)
app.component('DropDown', Dropdown)
app.component('PDialog', Dialog)
app.component('AutoComplete', AutoComplete)
app.component('FileUpload', FileUpload)
app.component('DataView', DataView)
app.component('SelectButton', SelectButton)
app.component('VueDatePicker', VueDatePicker);
app.mount('#app')
