function getPageInfo() {
    fetch('https://jsonmock.hackerrank.com/api/article_users?page=1')
        .then(response => response.json())
        .then(response => {
            const pageInfo = {
                page: response.page,
                per_page: response.per_page,
                total: response.total,
                total_pages: response.total_pages
            }
 
            const userInfo = [];
            const threshold = 10;
        
            for (let i = 1; i <= pageInfo.total_pages; i++) {
                userInfo.push(getUserInfo(userInfo, i))
            }
        
            const filteredUserInfo = userInfo.filter(user => user.submission_count > threshold).map(user => user.username);  
            console.log(filteredUserInfo)          
        })
        .catch(err => console.log(err));
 
    return pageInfo;
}

function getUserInfo(users, page){
    fetch(`https://jsonmock.hackerrank.com/api/article_users?page=${page}`)
    .then(response => response.json())
    .then(response => {
        let userInfo = response.data;
        users.push(userInfo);
    })
    .catch(err => console.log(err));
}