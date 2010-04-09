
from south.db import db
from django.db import models
from book.models import *

class Migration:
    
    def forwards(self, orm):
        
        # Adding model 'AuthorAlias'
        db.create_table('book_authoralias', (
            ('id', orm['book.AuthorAlias:id']),
            ('name', orm['book.AuthorAlias:name']),
        ))
        db.send_create_signal('book', ['AuthorAlias'])
        
        # Adding model 'Author'
        db.create_table('book_author', (
            ('id', orm['book.Author:id']),
            ('name', orm['book.Author:name']),
            ('credit', orm['book.Author:credit']),
        ))
        db.send_create_signal('book', ['Author'])
        
        # Adding model 'Annotation'
        db.create_table('book_annotation', (
            ('id', orm['book.Annotation:id']),
            ('name', orm['book.Annotation:name']),
        ))
        db.send_create_signal('book', ['Annotation'])
        
        # Adding model 'Series'
        db.create_table('book_series', (
            ('id', orm['book.Series:id']),
            ('name', orm['book.Series:name']),
        ))
        db.send_create_signal('book', ['Series'])
        
        # Adding model 'Language'
        db.create_table('book_language', (
            ('id', orm['book.Language:id']),
            ('short', orm['book.Language:short']),
            ('full', orm['book.Language:full']),
            ('full_national', orm['book.Language:full_national']),
        ))
        db.send_create_signal('book', ['Language'])
        
        # Adding model 'Tag'
        db.create_table('book_tag', (
            ('id', orm['book.Tag:id']),
            ('name', orm['book.Tag:name']),
        ))
        db.send_create_signal('book', ['Tag'])
        
        # Adding model 'BookFile'
        db.create_table('book_bookfile', (
            ('id', orm['book.BookFile:id']),
            ('link', orm['book.BookFile:link']),
            ('link_hash', orm['book.BookFile:link_hash']),
            ('time_found', orm['book.BookFile:time_found']),
            ('last_check', orm['book.BookFile:last_check']),
            ('size', orm['book.BookFile:size']),
            ('type', orm['book.BookFile:type']),
            ('more_info', orm['book.BookFile:more_info']),
            ('img_link', orm['book.BookFile:img_link']),
            ('credit', orm['book.BookFile:credit']),
        ))
        db.send_create_signal('book', ['BookFile'])
        
        # Adding model 'Book'
        db.create_table('book_book', (
            ('id', orm['book.Book:id']),
            ('title', orm['book.Book:title']),
            ('lang', orm['book.Book:lang']),
            ('language', orm['book.Book:language']),
            ('credit', orm['book.Book:credit']),
        ))
        db.send_create_signal('book', ['Book'])
        
        # Adding ManyToManyField 'Author.alias'
        db.create_table('book_author_alias', (
            ('id', models.AutoField(verbose_name='ID', primary_key=True, auto_created=True)),
            ('author', models.ForeignKey(orm.Author, null=False)),
            ('authoralias', models.ForeignKey(orm.AuthorAlias, null=False))
        ))
        
        # Adding ManyToManyField 'Book.annotation'
        db.create_table('book_book_annotation', (
            ('id', models.AutoField(verbose_name='ID', primary_key=True, auto_created=True)),
            ('book', models.ForeignKey(orm.Book, null=False)),
            ('annotation', models.ForeignKey(orm.Annotation, null=False))
        ))
        
        # Adding ManyToManyField 'Author.tag'
        db.create_table('book_author_tag', (
            ('id', models.AutoField(verbose_name='ID', primary_key=True, auto_created=True)),
            ('author', models.ForeignKey(orm.Author, null=False)),
            ('tag', models.ForeignKey(orm.Tag, null=False))
        ))
        
        # Adding ManyToManyField 'Book.series'
        db.create_table('book_book_series', (
            ('id', models.AutoField(verbose_name='ID', primary_key=True, auto_created=True)),
            ('book', models.ForeignKey(orm.Book, null=False)),
            ('series', models.ForeignKey(orm.Series, null=False))
        ))
        
        # Adding ManyToManyField 'Book.book_file'
        db.create_table('book_book_book_file', (
            ('id', models.AutoField(verbose_name='ID', primary_key=True, auto_created=True)),
            ('book', models.ForeignKey(orm.Book, null=False)),
            ('bookfile', models.ForeignKey(orm.BookFile, null=False))
        ))
        
        # Adding ManyToManyField 'Book.author'
        db.create_table('book_book_author', (
            ('id', models.AutoField(verbose_name='ID', primary_key=True, auto_created=True)),
            ('book', models.ForeignKey(orm.Book, null=False)),
            ('author', models.ForeignKey(orm.Author, null=False))
        ))
        
        # Adding ManyToManyField 'Book.tag'
        db.create_table('book_book_tag', (
            ('id', models.AutoField(verbose_name='ID', primary_key=True, auto_created=True)),
            ('book', models.ForeignKey(orm.Book, null=False)),
            ('tag', models.ForeignKey(orm.Tag, null=False))
        ))
        
    
    
    def backwards(self, orm):
        
        # Deleting model 'AuthorAlias'
        db.delete_table('book_authoralias')
        
        # Deleting model 'Author'
        db.delete_table('book_author')
        
        # Deleting model 'Annotation'
        db.delete_table('book_annotation')
        
        # Deleting model 'Series'
        db.delete_table('book_series')
        
        # Deleting model 'Language'
        db.delete_table('book_language')
        
        # Deleting model 'Tag'
        db.delete_table('book_tag')
        
        # Deleting model 'BookFile'
        db.delete_table('book_bookfile')
        
        # Deleting model 'Book'
        db.delete_table('book_book')
        
        # Dropping ManyToManyField 'Author.alias'
        db.delete_table('book_author_alias')
        
        # Dropping ManyToManyField 'Book.annotation'
        db.delete_table('book_book_annotation')
        
        # Dropping ManyToManyField 'Author.tag'
        db.delete_table('book_author_tag')
        
        # Dropping ManyToManyField 'Book.series'
        db.delete_table('book_book_series')
        
        # Dropping ManyToManyField 'Book.book_file'
        db.delete_table('book_book_book_file')
        
        # Dropping ManyToManyField 'Book.author'
        db.delete_table('book_book_author')
        
        # Dropping ManyToManyField 'Book.tag'
        db.delete_table('book_book_tag')
        
    
    
    models = {
        'book.annotation': {
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'name': ('django.db.models.fields.TextField', [], {'max_length': '10000'})
        },
        'book.author': {
            'alias': ('django.db.models.fields.related.ManyToManyField', [], {'to': "orm['book.AuthorAlias']", 'null': 'True', 'blank': 'True'}),
            'credit': ('django.db.models.fields.IntegerField', [], {'default': '0'}),
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'name': ('django.db.models.fields.CharField', [], {'unique': 'True', 'max_length': '255'}),
            'tag': ('django.db.models.fields.related.ManyToManyField', [], {'to': "orm['book.Tag']", 'null': 'True', 'blank': 'True'})
        },
        'book.authoralias': {
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'name': ('django.db.models.fields.CharField', [], {'unique': 'True', 'max_length': '255'})
        },
        'book.book': {
            'annotation': ('django.db.models.fields.related.ManyToManyField', [], {'to': "orm['book.Annotation']", 'null': 'True', 'blank': 'True'}),
            'author': ('django.db.models.fields.related.ManyToManyField', [], {'to': "orm['book.Author']", 'null': 'True', 'blank': 'True'}),
            'book_file': ('django.db.models.fields.related.ManyToManyField', [], {'to': "orm['book.BookFile']"}),
            'credit': ('django.db.models.fields.IntegerField', [], {'default': '0'}),
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'lang': ('django.db.models.fields.CharField', [], {'max_length': '2'}),
            'language': ('django.db.models.fields.related.ForeignKey', [], {'to': "orm['book.Language']"}),
            'series': ('django.db.models.fields.related.ManyToManyField', [], {'to': "orm['book.Series']", 'null': 'True', 'blank': 'True'}),
            'tag': ('django.db.models.fields.related.ManyToManyField', [], {'to': "orm['book.Tag']", 'null': 'True', 'blank': 'True'}),
            'title': ('django.db.models.fields.CharField', [], {'max_length': '255'})
        },
        'book.bookfile': {
            'credit': ('django.db.models.fields.IntegerField', [], {'default': '0'}),
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'img_link': ('django.db.models.fields.TextField', [], {'max_length': '4000', 'null': 'True', 'blank': 'True'}),
            'last_check': ('django.db.models.fields.DateTimeField', [], {'auto_now': 'True', 'blank': 'True'}),
            'link': ('django.db.models.fields.TextField', [], {'max_length': '4000'}),
            'link_hash': ('django.db.models.fields.CharField', [], {'unique': 'True', 'max_length': '32'}),
            'more_info': ('django.db.models.fields.TextField', [], {'max_length': '10000', 'null': 'True', 'blank': 'True'}),
            'size': ('django.db.models.fields.IntegerField', [], {'default': '0'}),
            'time_found': ('django.db.models.fields.DateTimeField', [], {'auto_now_add': 'True', 'blank': 'True'}),
            'type': ('django.db.models.fields.CharField', [], {'max_length': '10'})
        },
        'book.language': {
            'full': ('django.db.models.fields.CharField', [], {'unique': 'True', 'max_length': '255'}),
            'full_national': ('django.db.models.fields.CharField', [], {'max_length': '255'}),
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'short': ('django.db.models.fields.CharField', [], {'unique': 'True', 'max_length': '2'})
        },
        'book.series': {
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'name': ('django.db.models.fields.CharField', [], {'unique': 'True', 'max_length': '255'})
        },
        'book.tag': {
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'name': ('django.db.models.fields.CharField', [], {'unique': 'True', 'max_length': '255'})
        }
    }
    
    complete_apps = ['book']
