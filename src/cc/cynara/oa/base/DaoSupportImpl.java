package cc.cynara.oa.base;

import java.lang.reflect.ParameterizedType;
import java.util.Collections;
import java.util.List;
import javax.annotation.Resource;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("unchecked")
@Transactional
public abstract class DaoSupportImpl<T> implements DaoSupport<T> {
	@Resource
	private SessionFactory sessionFactory;

	protected Class<T> clazz = null;

	// public BaseDaoImpl(Class<T> clazz) {
	// this.clazz = clazz;
	// }

	// 通过默认构造方法
	public DaoSupportImpl() {
		ParameterizedType type = (ParameterizedType) this.getClass()
				.getGenericSuperclass();
		this.clazz = (Class<T>) type.getActualTypeArguments()[0];
	}

	// 获取当前可以用的session

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	public void save(T entity) {
		getSession().save(entity);
	}

	public void update(T entity) {
		getSession().update(entity);
	}

	public void delete(Long id) {
		if (id == null) {
			return;
		}
		Object entity = getById(id);
		if (entity != null) {
			getSession().delete(entity);
		}
	}

	public List<T> findAll() {
		return getSession().createQuery("from " + clazz.getSimpleName()).list();
	}

	public T getById(Long id) {
		if (id == null) {
			return null;
		} else {
			return (T) getSession().get(clazz, id);
		}
	}

	public List<T> getByIds(Long[] ids) {
		if(ids==null ||ids.length==0){
			return Collections.EMPTY_LIST;
		}
		return getSession().createQuery(
				"FROM " + clazz.getSimpleName() + " WHERE id IN (:ids)")//
				.setParameterList("ids", ids) // 一定要使用setParameterList
				.list();
	}

}
