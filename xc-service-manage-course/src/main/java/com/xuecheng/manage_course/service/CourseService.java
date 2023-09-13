package com.xuecheng.manage_course.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.CmsPostPageResult;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CourseInfo;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.domain.course.CoursePic;
import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.response.AddCourseResult;
import com.xuecheng.framework.domain.course.response.CourseCode;
import com.xuecheng.framework.domain.course.response.CoursepublishResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.client.CmsPageClient;
import com.xuecheng.manage_course.dao.*;
import io.swagger.annotations.ApiOperation;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.PathVariable;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CourseService {
    @Autowired
    CourseMapper courseMapper;
    @Autowired
    CourseBaseRepository courseBaseRepository;
    @Autowired
    CourseMarketRepository courseMarketRepository;
    @Autowired
    CoursePicRepository coursePicRepository;
    @Autowired
    TeachplanMapper teachplanMapper;
    @Autowired
    CmsPageClient cmsPageClient;


    @Value("${course-publish.dataUrlPre}")
    private String publish_dataUrlPre;
    @Value("${course-publish.pagePhysicalPath}")
    private String publish_page_physicalpath;
    @Value("${course-publish.pageWebPath}")
    private String publish_page_webpath;
    @Value("${course-publish.siteId}")
    private String publish_siteId;
    @Value("${course-publish.templateId}")
    private String publish_templateId;
    @Value("${course-publish.previewUrl}")
    private String previewUrl;








    //查询课程ID和name
    public QueryResponseResult getPageCourseBase(int page,int size){
        PageHelper.startPage(page,size);
        Page<CourseInfo> courseList = courseMapper.findCourseListInfo();
        System.out.println("--------------------------------------------------courseList:"+courseList);

        QueryResult<CourseInfo> courseInfoQueryResult = new QueryResult<CourseInfo>();
        //取页数
        long total = courseList.getTotal();
        courseInfoQueryResult.setList(courseList.getResult());
        courseInfoQueryResult.setTotal(total);
        return new QueryResponseResult(CommonCode.SUCCESS,courseInfoQueryResult);
    }
    //根据ID查询详细课程信息
    public CourseBase getCourseBaseById(String id){

        CourseBase courseBaseById = courseMapper.findCourseBaseById(id);
        //校验是否为空          定义Mapper时建议直接用optional
        System.out.println("--==================================================================="+courseBaseById);
        if (courseBaseById != null){
            return courseBaseById;
        }
        return null;
    }
    //添加课程
    public ResponseResult addCourseBase(CourseBase courseBase){

        return null;
    }

    //查询课程分类
    public List<CategoryNode> getCategory(String id){
        /*CategoryNode category = new CategoryNode();
                category.setChildren(courseMapper.getCategory(id));
//        if (category != null){
            return category;
//        }
//        return null;
*/
        //=====================================================================

        List<CategoryNode> category = courseMapper.getCategory(id);
        //最终传出的list
        ArrayList<CategoryNode> categoryNodesList = new ArrayList<>();

        //=-==================================================         .filter(item->!id.equals(item.getId()))排除根节点          、、？.filter(item->item.getId().equals(id))
        Map<String, CategoryNode> mapTmp = category.stream().filter(item->!id.equals(item.getId())).collect(Collectors.toMap(key -> key.getId(), value -> value, (key1, key2) -> key2));
        category.stream().forEach(item-> {
            //向list里写入元素
            if (item.getParentid().equals(id)) {
                categoryNodesList.add(item);
            }
            //找到子节点的父节点
            CategoryNode categoryNode = mapTmp.get(item.getParentid());
            if (categoryNode!=null){
                if (categoryNode.getChildren() == null) {
                //如果该父节点Children属性为空要new一个集合，因为要向该属性中放他的子节点
                categoryNode.setChildren(new ArrayList<CategoryNode>());

                                                         }
                //到每个节点的子节点放在父节点的Children属性中
                categoryNode.getChildren().add(item);
            }

        });
        return categoryNodesList;
    }





    //==============================================================================================================================================================
    //TODO                 待修改
/*    public List<ArrayList<CategoryNode>> getCategoryy(String id){
        List<ArrayList<CategoryNode>> children = new ArrayList<>();                   //----------------------解决返回值结构，与前端一致

        List<CategoryNode> category = courseMapper.getCategory(id);
        //最终传出的list
        ArrayList<CategoryNode> categoryNodesList = new ArrayList<>();

        //=-==================================================         .filter(item->!id.equals(item.getId()))排除根节点          、、？.filter(item->item.getId().equals(id))
        Map<String, CategoryNode> mapTmp = category.stream().filter(item->!id.equals(item.getId())).collect(Collectors.toMap(key -> key.getId(), value -> value, (key1, key2) -> key2));
        category.stream().forEach(item-> {
            //向list里写入元素
            if (item.getParentid().equals(id)) {
                categoryNodesList.add(item);
            }
            //找到子节点的父节点
            CategoryNode categoryNode = mapTmp.get(item.getParentid());
            if (categoryNode!=null){
                if (categoryNode.getChildren() == null) {
                    //如果该父节点Children属性为空要new一个集合，因为要向该属性中放他的子节点
                    categoryNode.setChildren(new ArrayList<CategoryNode>());

                }
                //到每个节点的子节点放在父节点的Children属性中
                categoryNode.getChildren().add(item);
            }

        });
        children.add(categoryNodesList);//----------------------解决返回值结构，与前端一致
//        return categoryNodesList;
        return children;
    }*/

    //新增课程
    @Transactional
    public AddCourseResult addCourse(CourseBase courseBase){
        courseBase.setStatus("202001");
        courseBaseRepository.save(courseBase);
        return new AddCourseResult(CommonCode.SUCCESS,courseBase.getId());
    }
    //获取课程基础信息
    public CourseBase GetCourseBaseById(String courseId){
        Optional<CourseBase> byId = courseBaseRepository.findById(courseId);
        if ( byId.isPresent()) {
         return byId.get();
        }
        return null;
    }

    //更新课程基础信息
    public ResponseResult updateCourseBaseById(String courseId, CourseBase courseBase) {

        CourseBase one = this.getCourseBaseById(courseId);
        if (one == null) {
            return new ResponseResult(CommonCode.NULL);
        }
        one.setName(courseBase.getName());
        one.setMt(courseBase.getMt());
        one.setSt(courseBase.getSt());
        one.setGrade(courseBase.getGrade());
        one.setStudymodel(courseBase.getStudymodel());
        one.setUsers(courseBase.getUsers());
        one.setDescription(courseBase.getDescription());
        //保存更新的内容
        CourseBase save = courseBaseRepository.save(one);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    //获取课程营销信息
    public CourseMarket getCourseMarketbyId(String courseId){
        Optional<CourseMarket> byId = courseMarketRepository.findById(courseId);
        if (byId.isPresent()) {
            return byId.get();
        }
        return null;
    }

    //更新课程营销信息
    @Transactional
    public CourseMarket updateCourseMarketById(String courseId, CourseMarket courseMarket) {

        CourseMarket one = this.getCourseMarketbyId(courseId);
        if (one != null) {
            one.setCharge(courseMarket.getCharge());
            one.setStartTime(courseMarket.getStartTime());//课程有效期，开始时间
            one.setEndTime(courseMarket.getEndTime());//课程有效期，结束时间
            one.setPrice(courseMarket.getPrice());
            one.setQq(courseMarket.getQq());
            one.setValid(courseMarket.getValid());
            courseMarketRepository.save(one);
        }else {
            //添加课程营销信息
            one = new CourseMarket();
            BeanUtils.copyProperties(courseMarket,one);
            //设置课程ID
            one.setId(courseId);
            courseMarketRepository.save(one);
        }
        return one;
    }
        //向课程管理数据库添加课程与图片的关联信息
    @Transactional
    public ResponseResult addCoursePic(String courseId, String pic) {
        Optional<CoursePic> byId = coursePicRepository.findById(courseId);
        CoursePic coursePic = null;
        if (byId.isPresent()) {
            byId.get();
        }
        if (coursePic == null) {
            coursePic = new CoursePic();
        }
        coursePic.setCourseid(courseId);
        coursePic.setPic(pic);
//        CoursePic save =
        coursePicRepository.save(coursePic);
        return new ResponseResult(CommonCode.SUCCESS);


    }
    //在MySQL中查找课程图片信息
    public CoursePic findCoursePic(@PathVariable("courseId") String courseId) {
        Optional<CoursePic> byId = coursePicRepository.findById(courseId);
        if (byId.isPresent()) {
            CoursePic coursePic = byId.get();
            return coursePic;
        }
        return null;
    }
    //删除课程图片
    @Transactional
    public ResponseResult deleteCoursePic(String courseId) {
        /*coursePicRepository.deleteById(courseId);
        Optional<CoursePic> findPic = coursePicRepository.findById(courseId);
        if (findPic.isPresent()) {
            //查到有这个绑定的图片，删除失败

        }else
            //删除成功*/
        //上面让delete返回删除结果，在coursePicRepository处理
        long deleteResult = coursePicRepository.deleteByCourseid(courseId);
        if (deleteResult > 0) {
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }
     //课程视图数据查询
    public CourseView getCourseView(String id) {
        //查询数据存入courseView
        CourseView courseView = new CourseView();
        Optional<CourseBase> courseBase = courseBaseRepository.findById(id);
        if (courseBase.isPresent()) {
            courseView.setCourseBase(courseBase.get());
        }
        Optional<CoursePic> coursePic = coursePicRepository.findById(id);
        if (coursePic.isPresent()) {
            courseView.setCoursePic(coursePic.get());
        }
        Optional<CourseMarket> courseMarket = courseMarketRepository.findById(id);
        if (courseMarket.isPresent()) {
            courseView.setCourseMarket(courseMarket.get());
        }
        TeachplanNode teachplanNode = teachplanMapper.selectList(id);
        courseView.setTeachplanNode(teachplanNode);
        return courseView;
    }

    //根据id查询课程基本信息
    public CourseBase findCourseBaseById(String courseId){
        Optional<CourseBase> baseOptional = courseBaseRepository.findById(courseId);
        if(baseOptional.isPresent()){
            CourseBase courseBase = baseOptional.get();
            return courseBase;
        }
        ExceptionCast.cast(CourseCode.COURSE_GET_NOTEXISTS);
        return null;
    }


    //课程预览页面
    public CoursepublishResult preview(String id) {
        //查询课程
        CourseBase courseBaseById = this.findCourseBaseById(id);     //课程信息
        CmsPage cmsPage = new CmsPage();
        cmsPage.setSiteId(publish_siteId);
        cmsPage.setDataUrl(publish_dataUrlPre+id);
        cmsPage.setPageName(id+".html");
        cmsPage.setPageAliase(courseBaseById.getName());       //页面别名
        cmsPage.setPagePhysicalPath(publish_page_physicalpath);
        cmsPage.setPageWebPath(publish_page_webpath);
        cmsPage.setTemplateId(publish_templateId);
        //请求cms添加页面,远程调用
        CmsPageResult cmsPageResult = cmsPageClient.saveCmsPage(cmsPage);
        if (!cmsPageResult.isSuccess()){
            //异常
            return new CoursepublishResult(CommonCode.FAIL,null);
        }
        CmsPage cmsPage1 = cmsPageResult.getCmsPage();
        String pageId = cmsPage1.getPageId();
        //拼装页面url
        String url = previewUrl+pageId;
        //返回值
        return new CoursepublishResult(CommonCode.SUCCESS,url);

    }
        //页面发布
    @Transactional
    public CoursepublishResult publish(String id) {
        //查询课程
        CourseBase courseBaseById = this.findCourseBaseById(id);     //课程信息
        CmsPage cmsPage = new CmsPage();
        cmsPage.setSiteId(publish_siteId);
        cmsPage.setDataUrl(publish_dataUrlPre+id);
        cmsPage.setPageName(id+".html");
        cmsPage.setPageAliase(courseBaseById.getName());       //页面别名
        cmsPage.setPagePhysicalPath(publish_page_physicalpath);
        cmsPage.setPageWebPath(publish_page_webpath);
        cmsPage.setTemplateId(publish_templateId);
        //调用cms接口一键发布接口
        CmsPostPageResult cmsPostPageResult = cmsPageClient.postPageQuick(cmsPage);
        String pageUrl = cmsPostPageResult.getPageUrl();                            //CMS返回的URL
        //保存课程状态为已发布
        if (!cmsPostPageResult.isSuccess()) {
            return new CoursepublishResult(CommonCode.FAIL,null);
        }
        CourseBase courseBase = this.saveCourseStat(id);
        if (courseBase == null) {
            return new CoursepublishResult(CommonCode.NULL,null);
        }
        //保存课程索引信息

        //缓存课程信息
        //....

        return new CoursepublishResult(CommonCode.SUCCESS,pageUrl);
    }
    //更改课程状态          已发布  202002
    public CourseBase saveCourseStat(String courseId){
        CourseBase courseBaseById = this.findCourseBaseById(courseId);
        courseBaseById.setStatus("202002");
        courseBaseRepository.save(courseBaseById);
        return courseBaseById;
    }
}
