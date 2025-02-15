package uestc.service.impl;

import uestc.entity.Department;
import uestc.mapper.DepartmentMapper;
import uestc.service.DepartmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liulong
 * @since 2025-01-25
 */
@Service
public class DepartmentServiceImp extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {

}
