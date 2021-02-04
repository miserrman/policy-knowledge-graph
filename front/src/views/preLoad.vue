<template>
    <div class="bg">
        <img style="margin-top:150px;margin-left:550px;" src="@/assets/preLoad.gif" alt="Loading">
        <br/>
        <span style="margin-top:150px;margin-left:600px;font-size:24px;color:#9A32CD">Loading……</span>
    </div>
</template>

<script>
export default {
    name:'preLoad'
}
</script>

<style scoped>
    .bg{
        position: absolute;
        width:100%;
        height:100%;
        top:0;
        left:0;
        background-color:#D1D1D1
    }
</style>

