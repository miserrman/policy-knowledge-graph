<template>
    <div style="height: 300px;">
        <Header></Header>
        <sidebar></sidebar>
    </div>
</template>

<script>
import Header from '@/components/header'
import sidebar from '@/views/UserPage/sidebar'

export default {
    name:'userpage',
    components:{
        Header,sidebar
    }
}
</script>
