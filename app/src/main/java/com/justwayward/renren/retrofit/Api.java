package com.justwayward.renren.retrofit;

import com.justwayward.renren.bean.AboutBean;
import com.justwayward.renren.bean.BannerBean;
import com.justwayward.renren.bean.BookBean;
import com.justwayward.renren.bean.BookCityBean;
import com.justwayward.renren.bean.BookCityCategoryBean;
import com.justwayward.renren.bean.BookDetailBean;
import com.justwayward.renren.bean.BookShelfBean;
import com.justwayward.renren.bean.BuyHistoryBean;
import com.justwayward.renren.bean.CalPriceBean;
import com.justwayward.renren.bean.CategoryBean;
import com.justwayward.renren.bean.CategoryListBean;
import com.justwayward.renren.bean.ChapterListBean;
import com.justwayward.renren.bean.CoinRateBean;
import com.justwayward.renren.bean.CommonBean;
import com.justwayward.renren.bean.CopyrightBean;
import com.justwayward.renren.bean.DiscoverBean;
import com.justwayward.renren.bean.FeedBackBean;
import com.justwayward.renren.bean.FontListBean;
import com.justwayward.renren.bean.FreeBean;
import com.justwayward.renren.bean.FreeListBean;
import com.justwayward.renren.bean.HotSearchBean;
import com.justwayward.renren.bean.LoginBean;
import com.justwayward.renren.bean.MonthPackageBean;
import com.justwayward.renren.bean.PayBean;
import com.justwayward.renren.bean.RechargeListBean;
import com.justwayward.renren.bean.RechargeMoneyBean;
import com.justwayward.renren.bean.RechargeRecordBean;
import com.justwayward.renren.bean.ReviewBean;
import com.justwayward.renren.bean.ReviewDetailBean;
import com.justwayward.renren.bean.SearchBean;
import com.justwayward.renren.bean.ShareBean;
import com.justwayward.renren.bean.SiteListBean;
import com.justwayward.renren.bean.SourceBean;
import com.justwayward.renren.bean.SubZoneBean;
import com.justwayward.renren.bean.SwitchBean;
import com.justwayward.renren.bean.TopDetailBean;
import com.justwayward.renren.bean.UserBean;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by admin on 2017/3/27.
 */

public interface Api {

    //注册
    @FormUrlEncoded
    @POST("/api/public/register")
    Observable<HttpResult<CommonBean>> register(@Field("mobile") String mobile,
                                                @Field("password") String password,
                                                @Field("code") String code);

    //获取验证码
    @FormUrlEncoded
    @POST("/api/public/sendsms")
    Observable<HttpResult<CommonBean>> sendsms(@Field("mobile") String mobile, @Field("type") String type);

    //登录
    @FormUrlEncoded
    @POST("/api/public/login")
    Observable<HttpResult<LoginBean>> login(@Field("mobile") String mobile,
                                            @Field("password") String password);

    //找回密码
    @FormUrlEncoded
    @POST("/api/public/forgetpwd")
    Observable<HttpResult<CommonBean>> forgetpwd(@Field("mobile") String mobile,
                                                 @Field("password") String password,
                                                 @Field("code") String code);

    //修改密码
    @FormUrlEncoded
    @POST("/api/user/modifypwd")
    Observable<HttpResult<CommonBean>> modifypwd(@Field("token") String token,
                                                 @Field("oldpassword") String oldpassword,
                                                 @Field("password") String password);

    //获取轮播图
    @FormUrlEncoded
    @POST("/api/ad/getAdList")
    Observable<HttpResult<List<BannerBean>>> getAdList(@Field("slide_id") String token);

    //热搜
    @FormUrlEncoded
    @POST("/api/search/getSearchHot")
    Observable<HttpResult<HotSearchBean>> getSearchHot(@Field("page") int page);

    //热门推荐
    @FormUrlEncoded
    @POST("/api/search/getSearchRecommend")
    Observable<HttpResult<HotSearchBean>> getSearchRecommend(@Field("page") int page);

    //搜索作者或书名
    @FormUrlEncoded
    @POST("/api/search/searchList")
    Observable<HttpResult<SearchBean>> searchList(@Field("searchword") String searchword);

    //书籍详情
    @FormUrlEncoded
    @POST("/api/novel/getNovelInfo")
    Observable<HttpResult<BookDetailBean>> getNovelInfo(@Field("token") String token, @Field("novel_id") String novel_id);

    //添加书架
    @FormUrlEncoded
    @POST("/api/bookshelf/addNovel")
    Observable<HttpResult<CommonBean>> addNovel(@Field("token") String token, @Field("novel_id") String novel_id);

    //删除书架
    @FormUrlEncoded
    @POST("/api/bookshelf/delBook")
    Observable<HttpResult<CommonBean>> delBook(@Field("token") String token, @Field("novel_ids") String novel_id);

    //书架列表
    @FormUrlEncoded
    @POST("/api/bookshelf/getList")
    Observable<HttpResult<BookShelfBean>> getList(@Field("token") String token);

    //置顶
    @FormUrlEncoded
    @POST("/api/bookshelf/setTop")
    Observable<HttpResult<CommonBean>> setTop(@Field("token") String token, @Field("id") String id);

    //取消置顶
    @FormUrlEncoded
    @POST("/api/bookshelf/cancelTop")
    Observable<HttpResult<CommonBean>> cancelTop(@Field("token") String token, @Field("id") String id);

    //获取章节列表
    @FormUrlEncoded
    @POST("/api/novel/getChapterList")
    Observable<HttpResult<List<ChapterListBean>>> getChapterList(@Field("token") String token,@Field("novel_id") String id);

    //获取章节内容
    @FormUrlEncoded
    @POST("/api/novel/getChapterContent")
    Observable<HttpResult<BookBean>> getChapterContent(@Field("token") String token, @Field("id") String id);

    //全部书评
    @FormUrlEncoded
    @POST("/api/comment/getListByNovel")
    Observable<HttpResult<ReviewBean>> getReviewList(@Field("novel_id") String id, @Field("page") int page);

    //添加书评
    @FormUrlEncoded
    @POST("/api/comment/addComment")
    Observable<HttpResult<CommonBean>> addComment(@Field("token") String token,
                                                  @Field("novel_id") String id,
                                                  @Field("score") String score,
                                                  @Field("title") String title,
                                                  @Field("comment") String comment);

    //书评详情
    @FormUrlEncoded
    @POST("/api/comment/getCommentInfo")
    Observable<HttpResult<ReviewDetailBean>> getCommentInfo(@Field("token") String token, @Field("id") String id);

    //书评评论
    @FormUrlEncoded
    @POST("/api/comment/addReply")
    Observable<HttpResult<CommonBean>> addReply(@Field("token") String token,
                                                @Field("id") String id,
                                                @Field("novel_id") String novel_id,
                                                @Field("content") String content);


    //获取分类列表
    @FormUrlEncoded
    @POST("/api/category/getList")
    Observable<HttpResult<List<BookCityCategoryBean>>> getCategoryList(@Field("pid") String pid);

    //获取首页今日限免小说
    @FormUrlEncoded
    @POST("/api/novel/getTodayFree")
    Observable<HttpResult<FreeBean>> getTodyFree(@Field("token") String token);

    //首页推荐小说
    @FormUrlEncoded
    @POST("/api/novel/getIndexRecommend")
    Observable<HttpResult<List<BookCityBean>>> getIndexRecommend(@Field("token") String token);

    //分类
    @FormUrlEncoded
    @POST("/api/category/getCategoryStatistical")
    Observable<HttpResult<List<CategoryBean>>> getCategoryStatistical(@Field("token") String token);

    //获取一级分类小说列表
    @FormUrlEncoded
    @POST("/api/novel/getNovelByCategoryIndex")
    Observable<HttpResult<List<BookCityBean>>> getNovelByCategoryIndex(@Field("category_id") String category_id);

    //获取充值兑换比例
    @FormUrlEncoded
    @POST("/api/Recharge/getCoinRate")
    Observable<HttpResult<CoinRateBean>> getCoinRate(@Field("token") String token);

    //获取充值优惠
    @FormUrlEncoded
    @POST("/api/Recharge/getRechargePackage")
    Observable<HttpResult<List<RechargeListBean>>> getRechargePackage(@Field("token") String token);

    //获取充值金额数
    @FormUrlEncoded
    @POST("/api/public/getRechargeMoney")
    Observable<RechargeMoneyBean> getRechargeMoney(@Field("token") String token);

    //获取包月套餐
    @FormUrlEncoded
    @POST("/api/Recharge/getMonthPackage")
    Observable<HttpResult<List<MonthPackageBean>>> getMonthPackage(@Field("token") String token);

    //获取充值记录
    @FormUrlEncoded
    @POST("/api/Recharge/getRechargeList")
    Observable<HttpResult<List<RechargeRecordBean>>> getPay(@Field("token") String token,@Field("type") String type);

    //充值或购买会员
    @FormUrlEncoded
    @POST("/api/Recharge/pay")
    Observable<HttpResult<PayBean>> pay(@Field("token") String token,
                                        @Field("item") String item,
                                        @Field("money") String money,
                                        @Field("package_id") String packages_id,
                                        @Field("pay_type") String pay_type);

    //获取小说分类，都市，现言，其他
    @FormUrlEncoded
    @POST("/api/category/getList")
    Observable<HttpResult<List<BookCityCategoryBean>>> getCategory(@Field("token") String token,@Field("is_recommend") int is_recommend);

    //获取小说分类，都市，现言，其他
    @FormUrlEncoded
    @POST("/api/category/getList")
    Observable<HttpResult<List<BookCityCategoryBean>>> getCategory2(@Field("is_recommend") int is_recommend);

    //获取热门 最新 完结
    @FormUrlEncoded
    @POST("/api/novel/getNovelByCategory")
    Observable<HttpResult<CategoryListBean>> getNovelByCategory(@FieldMap Map<String, Object> map, @Field("page") int page);

    //获取用户信息
    @FormUrlEncoded
    @POST("/api/User/getUserInfo")
    Observable<HttpResult<UserBean>> getUser(@Field("token") String token);

    //设置用户喜好
    @FormUrlEncoded
    @POST("/api/User/setUserLike")
    Observable<HttpResult<CommonBean>> setUserLike(@Field("token") String token, @Field("like_type") int like_type);

    //更新用户信息
    @FormUrlEncoded
    @POST("/api/User/updateUserInfo")
    Observable<HttpResult<CommonBean>> updateUserInfo(@Field("token") String token, @Field("field") String field, @Field("field_value") String field_value);

    //更新用户头像
    @FormUrlEncoded
    @POST("/api/User/setBookshelfSort")
    Observable<HttpResult<CommonBean>> setBookshelfSort(@Field("token") String token, @Field("bookshelf_sort") String bookshelf_sort);


    //获取专区列表
    @FormUrlEncoded
    @POST("/api/zone/getList")
    Observable<HttpResult<List<DiscoverBean>>> getZoneList(@Field("token") String token);

    //获取分类小说排行榜
    @FormUrlEncoded
    @POST("/api/novel/getRankList")
    Observable<HttpResult<List<TopDetailBean>>> getRankList(@FieldMap Map<String, Object> map);


    //获取专区列表
    @FormUrlEncoded
    @POST("/api/public/getAbout")
    Observable<HttpResult<AboutBean>> getAbout(@Field("token") String token);

    //获取小说源
    @FormUrlEncoded
    @POST("/api/novel/getSource")
    Observable<HttpResult<SourceBean>> getSource(@Field("novel_id") String novel_id, @Field("chapter") String chapter);

    //获取章节内容
    @FormUrlEncoded
    @POST("/api/novel/getChapterContent")
    Observable<HttpResult<BookBean>> getChapterContent(@FieldMap Map<String, Object> map);

    //获取分享链接
    @FormUrlEncoded
    @POST("/api/public/getSettingInfo")
    Observable<HttpResult<ShareBean>> getSettingInfo(@Field("token") String token);

    //获取会员开关
    @FormUrlEncoded
    @POST("/api/public/getMemberSwitch")
    Observable<HttpResult<SwitchBean>> getMemberSwitch(@Field("token") String token);

    //获取反馈类型
    @FormUrlEncoded
    @POST("/api/feedback/getType")
    Observable<HttpResult<List<FeedBackBean>>> getType(@Field("token") String token);

    //更新阅读时间
    @FormUrlEncoded
    @POST("/api/bookshelf/updateReadTime")
    Observable<HttpResult<CommonBean>> updateReadTime(@Field("token") String token, @Field("novel_id") String novel_id);

    //支付章节
    @FormUrlEncoded
    @POST("/api/novel/payChapter")
    Observable<HttpResult> payChapter(@Field("token") String token, @Field("chapter_id") String chapter_id, @Field("chapter_num") int chapter_num);

    //反馈
    @FormUrlEncoded
    @POST("/api/feedback/add")
    Observable<HttpResult<CommonBean>> feedback(@Field("token") String token, @Field("type") String type, @Field("content") String content);

    //点赞
    @FormUrlEncoded
    @POST("/api/comment/praise")
    Observable<HttpResult<CommonBean>> praise(@Field("token") String token, @Field("novel_id") String novel_id, @Field("comment_id") String comment_id);

    //获取反馈类型
    @FormUrlEncoded
    @POST("/api/public/quickLogin")
    Observable<HttpResult<LoginBean>> quickLogin(@Field("type") String type,
                                                 @Field("openid") String openid,
                                                 @Field("avatar") String avatar,
                                                 @Field("user_nickname") String user_nickname);
    //获取站点列表
    @FormUrlEncoded
    @POST("/api/novel/getSiteList")
    Observable<HttpResult<List<SiteListBean>>> getSiteList(@Field("token") String token);

    //获取版权列表
    @FormUrlEncoded
    @POST("/api/novel/getCopyrightList")
    Observable<CopyrightBean> getCopyrightList(@Field("token") String token);


    //获取站点列表
    @FormUrlEncoded
    @POST("/api/public/getFontList")
    Observable<HttpResult<List<FontListBean>>> getFontList(@Field("token") String token);

    //获取站点列表
    @FormUrlEncoded
    @POST("/api/Recharge/getCoinLog")
    Observable<HttpResult<BuyHistoryBean>> getCoinLog(@Field("token") String token);

    //获取指定专区下的所有分类表列
    @FormUrlEncoded
    @POST("/api/zone/getAllSubZone")
    Observable<HttpResult<List<SubZoneBean>>> getAllSubZone(@Field("pid") String pid);

    //计算缓存章节金额
    @FormUrlEncoded
    @POST("/api/Recharge/calculatePrice")
    Observable<HttpResult<CalPriceBean>> calculatePrice(@Field("token") String token, @Field("chapter_id") String chapter_id, @Field("chapter_num") int chapter_num);



    //获取指定专区下的所有分类表列
    @FormUrlEncoded
    @POST("/api/novel/getLimitTimeFree")
    Observable<HttpResult<List<FreeListBean>>> getLimitTimeFree(@Field("pid") String pid);


}